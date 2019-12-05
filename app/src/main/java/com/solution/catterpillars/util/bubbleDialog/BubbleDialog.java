package com.solution.catterpillars.util.bubbleDialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.solution.catterpillars.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;



public class BubbleDialog extends Dialog
{

    public enum Position
    {

        LEFT,

        TOP,

        RIGHT,

        BOTTOM
    }

    private BubbleLayout mBubbleLayout;
    private int mWidth, mHeight, mMargin;
    private View mAddView;
    private View mClickedView;
    private boolean mCalBar;
    private int mOffsetX, mOffsetY;
    private int mRelativeOffset;
    private boolean mSoftShowUp;
    private Position mPosition = Position.TOP;
    private Position[] mPositions = new Position[4];
    private Auto mAuto;
    private boolean isThroughEvent = false;
    private boolean mCancelable;
    private int[] clickedViewLocation = new int[2];
    private Activity mActivity;
    private ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;

    public BubbleDialog(Context context)
    {
        super(context, R.style.bubble_dialog);
        setCancelable(true);

        mActivity = (Activity) context;
        Window window = getWindow();
        if (window == null) return;
        final WindowManager.LayoutParams params = window.getAttributes();
        final int screenW = Util.getScreenWH(getContext())[0];
        final int statusBar = Util.getStatusHeight(getContext());
        getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (isThroughEvent)
                {
                    float x = params.x < 0 ? 0 : params.x;
                    x = x + v.getWidth() > screenW ? screenW - v.getWidth() : x;

                    x += event.getX();
                    float y = params.y + event.getY() + (mCalBar ? statusBar : 0);

//                LogUtil.e2(String.format("(%s, %s) > (%s, %s)", event.getX(), event.getY(), x, y));
                    event.setLocation(x, y);
                    mActivity.getWindow().getDecorView().dispatchTouchEvent(event);
                    return true;
                } else
                {
                    return false;
                }
            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (isThroughEvent && keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            dismiss();
            mActivity.onBackPressed();
            mActivity = null;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (mBubbleLayout == null)
        {
            mBubbleLayout = new BubbleLayout(getContext());
        }
        if (mAddView != null)
        {
            mBubbleLayout.addView(mAddView);
        }
        setContentView(mBubbleLayout);

        final Window window = getWindow();
        if (window == null) return;
        if (mSoftShowUp)
        {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        }
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        onAutoPosition();

        setLook();
//        mBubbleLayout.post(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                dialogPosition();
//            }
//        });

        mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            int lastWidth, lastHeight;
            @Override
            public void onGlobalLayout()
            {
                if (lastWidth == mBubbleLayout.getWidth() && lastHeight == mBubbleLayout.getHeight()) return;
                dialogPosition();
                lastWidth = mBubbleLayout.getWidth();
                lastHeight = mBubbleLayout.getHeight();
            }
        };

        mBubbleLayout.getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);


        mBubbleLayout.setOnClickEdgeListener(new BubbleLayout.OnClickEdgeListener()
        {
            @Override
            public void edge()
            {
                if (BubbleDialog.this.mCancelable)
                {
                    dismiss();
                }
            }
        });
    }


    private void onAutoPosition()
    {
        if (mClickedView == null || (mAuto == null && mPositions.length <= 1)) return;

        final int[] spaces = new int[4];
        spaces[0] = clickedViewLocation[0];
        spaces[1] = clickedViewLocation[1];
        spaces[2] = Util.getScreenWH(getContext())[0] - clickedViewLocation[0] - mClickedView.getWidth();
        spaces[3] = Util.getScreenWH(getContext())[1] - clickedViewLocation[1] - mClickedView.getHeight() - (mCalBar ? Util.getStatusHeight(getContext()) : 0);

        if (mPositions.length > 1) {
            mAddView.measure(0, 0);
            for (Position p : mPositions) {
                if (p == null) return;
                switch (p) {
                    case LEFT:
                        if (spaces[0] > mAddView.getMeasuredWidth()) {
                            mPosition = Position.LEFT;
                            return;
                        }
                        break;
                    case TOP:
                        if (spaces[1] > mAddView.getMeasuredHeight()) {
                            mPosition = Position.TOP;
                            return;
                        }
                        break;
                    case RIGHT:
                        if (spaces[2] > mAddView.getMeasuredWidth()) {
                            mPosition = Position.RIGHT;
                            return;
                        }
                        break;
                    case BOTTOM:
                        if (spaces[3] > mAddView.getMeasuredHeight()) {
                            mPosition = Position.BOTTOM;
                            return;
                        }
                        break;
                }
            }
            mPosition = mPositions[0];
            return;
        }
        if (mAuto != null)
        {
            switch (mAuto)
            {
                case AROUND:
                    break;
                case UP_AND_DOWN:
                    mPosition = spaces[1] > spaces[3] ? Position.TOP : Position.BOTTOM;
                    return;
                case LEFT_AND_RIGHT:
                    mPosition = spaces[0] > spaces[2] ? Position.LEFT : Position.RIGHT;
                    return;
                default:
            }
        }


        int max = 0;
        for (int value : spaces)
        {
            if (value > max) max = value;
        }

        if (max == spaces[0])
        {
            mPosition = Position.LEFT;
        } else if (max == spaces[1])
        {
            mPosition = Position.TOP;
        } else if (max == spaces[2])
        {
            mPosition = Position.RIGHT;
        } else if (max == spaces[3])
        {
            mPosition = Position.BOTTOM;
        }
    }

    private void setLook()
    {
        switch (mPosition)
        {
            case LEFT:
                mBubbleLayout.setLook(BubbleLayout.Look.RIGHT);
                break;
            case TOP:
                mBubbleLayout.setLook(BubbleLayout.Look.BOTTOM);
                break;
            case RIGHT:
                mBubbleLayout.setLook(BubbleLayout.Look.LEFT);
                break;
            case BOTTOM:
                mBubbleLayout.setLook(BubbleLayout.Look.TOP);
                break;
        }
        mBubbleLayout.initPadding();
    }

    @Override
    public void dismiss()
    {
        if (mSoftShowUp)
        {
            Util.hide(BubbleDialog.this);
        }
        if (mBubbleLayout != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            mBubbleLayout.getViewTreeObserver().removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
        super.dismiss();
    }

    private void dialogPosition()
    {
        if (mClickedView == null)
        {
            return;
        }

        Window window = getWindow();
        if (window == null) return;
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        FrameLayout.LayoutParams bubbleParams = null;

//        if (mWidth != 0 || mHeight != 0)
//        {
//            ViewGroup.LayoutParams bubbleParams = mBubbleLayout.getLayoutParams();
//            bubbleParams.width = MATCH_PARENT;
//            bubbleParams.height = MATCH_PARENT;
//            mBubbleLayout.setLayoutParams(bubbleParams);
//        }

        if (mWidth != 0)
        {
            params.width = mWidth;
        }

        if (mHeight != 0)
        {
            params.height = mHeight;
        }

        if (mMargin != 0)
        {
            bubbleParams = (FrameLayout.LayoutParams) mBubbleLayout.getLayoutParams();
            if (mPosition == Position.TOP || mPosition == Position.BOTTOM)
            {
                bubbleParams.leftMargin = mMargin;
                bubbleParams.rightMargin = mMargin;
            } else
            {
                bubbleParams.topMargin = mMargin;
                bubbleParams.bottomMargin = mMargin;
            }
            mBubbleLayout.setLayoutParams(bubbleParams);
        }

        switch (mPosition)
        {
            case TOP:
            case BOTTOM:
                params.x = clickedViewLocation[0] + mClickedView.getWidth() / 2 - mBubbleLayout.getWidth() / 2 + mOffsetX;
                if (mMargin != 0 && mWidth == MATCH_PARENT)
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[0] - mMargin + mClickedView.getWidth() / 2 - mBubbleLayout.getLookWidth() / 2);
                } else if (params.x <= 0)
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[0] + mClickedView.getWidth() / 2 - mBubbleLayout.getLookWidth() / 2);
                } else if (params.x + mBubbleLayout.getWidth() > Util.getScreenWH(getContext())[0])
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[0] - (Util.getScreenWH(getContext())[0] - mBubbleLayout.getWidth()) + mClickedView.getWidth() / 2 - mBubbleLayout.getLookWidth() / 2);
                } else
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[0] - params.x + mClickedView.getWidth() / 2 - mBubbleLayout.getLookWidth() / 2);
                }
                if (mPosition == Position.BOTTOM)
                {
                    if (mRelativeOffset != 0) mOffsetY = mRelativeOffset;
                    params.y = clickedViewLocation[1] - (mCalBar ? Util.getStatusHeight(getContext()) : 0) + mClickedView.getHeight() + mOffsetY;

                } else
                {
                    if (mRelativeOffset != 0) mOffsetY = -mRelativeOffset;
                    params.y = clickedViewLocation[1] - (mCalBar ? Util.getStatusHeight(getContext()) : 0) - mBubbleLayout.getHeight() + mOffsetY;
                }
                break;
            case LEFT:
            case RIGHT:
                params.y = clickedViewLocation[1] - (mCalBar ? Util.getStatusHeight(getContext()) : 0) + mOffsetY + mClickedView.getHeight() / 2 - mBubbleLayout.getHeight() / 2;
                if (mMargin != 0 && mHeight == MATCH_PARENT)
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[1] - mMargin + mClickedView.getHeight() / 2 - mBubbleLayout.getLookWidth() / 2 - (mCalBar ? Util.getStatusHeight(getContext()) : 0));
                } else if (params.y <= 0)
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[1] + mClickedView.getHeight() / 2 - mBubbleLayout.getLookWidth() / 2 - (mCalBar ? Util.getStatusHeight(getContext()) : 0));
                } else if (params.y + mBubbleLayout.getHeight() > Util.getScreenWH(getContext())[1])
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[1] - (Util.getScreenWH(getContext())[1] - mBubbleLayout.getHeight()) + mClickedView.getHeight() / 2 - mBubbleLayout.getLookWidth() / 2);
                } else
                {
                    mBubbleLayout.setLookPosition(clickedViewLocation[1] - params.y + mClickedView.getHeight() / 2 - mBubbleLayout.getLookWidth()/ 2 - (mCalBar ? Util.getStatusHeight(getContext()) : 0));
                }
                if (mPosition == Position.RIGHT)
                {
                    if (mRelativeOffset != 0) mOffsetX = mRelativeOffset;
                    params.x = clickedViewLocation[0] + mClickedView.getWidth() + mOffsetX;
                } else
                {
                    if (mRelativeOffset != 0) mOffsetX = -mRelativeOffset;
                    params.x = clickedViewLocation[0] -  mBubbleLayout.getWidth() + mOffsetX;
                }
                break;
        }


        mBubbleLayout.invalidate();
        window.setAttributes(params);
    }

    public boolean onTouchEvent(MotionEvent event) {
        Window window = getWindow();

        if (window == null) return false;
        final View decorView = window.getDecorView();
        if (this.mCancelable && isShowing() && shouldCloseOnTouch(event, decorView)) {
            cancel();
            return true;
        }
        return false;
    }

    public boolean shouldCloseOnTouch(MotionEvent event, View decorView) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        return (x <= 0) || (y <= 0)
                || (x > (decorView.getWidth()))
                || (y > (decorView.getHeight()));
    }

    public void setCancelable(boolean flag)
    {
        super.setCancelable(flag);
        mCancelable = flag;
    }



    public <T extends BubbleDialog> T setLayout(int width, int height, int margin)
    {
        mWidth = width;
        mHeight = height;
        mMargin = margin;
        return (T) this;
    }


    public <T extends BubbleDialog> T calBar(boolean cal)
    {
        this.mCalBar = cal;
        return (T) this;
    }


    public <T extends BubbleDialog> T setClickedView(View view)
    {
        this.mClickedView = view;
        mClickedView.getLocationOnScreen(clickedViewLocation);
        if (mOnGlobalLayoutListener != null)
        {
            onAutoPosition();
            setLook();
            dialogPosition();
        }
        return (T) this;
    }


    public <T extends BubbleDialog> T softShowUp()
    {
        this.mSoftShowUp = true;
        return (T) this;
    }


    public <T extends BubbleDialog> T addContentView(View view)
    {
        this.mAddView = view;
        return (T) this;
    }


    public <T extends BubbleDialog> T setPosition(Position ... positions)
    {
        if (positions.length == 1) {
            this.mPosition = positions[0];
            return (T) this;
        }
        this.mPositions = positions;
        return (T) this;
    }


    public <T extends BubbleDialog> T autoPosition(Auto auto)
    {
        this.mAuto = auto;
        return (T) this;
    }


    public <T extends BubbleDialog> T setThroughEvent(boolean isThroughEvent, boolean cancelable)
    {
        this.isThroughEvent = isThroughEvent;
        if (isThroughEvent)
        {
            setCancelable(false);
        } else
        {
            setCancelable(cancelable);
        }
        return (T) this;
    }


    public <T extends BubbleDialog> T setOffsetX(int offsetX)
    {
        this.mOffsetX = Util.dpToPx(getContext(), offsetX);
        return (T) this;
    }


    public <T extends BubbleDialog> T setOffsetY(int offsetY)
    {
        this.mOffsetY = Util.dpToPx(getContext(), offsetY);
        return (T) this;
    }


    public <T extends BubbleDialog> T setRelativeOffset(int relativeOffset)
    {
        this.mRelativeOffset = Util.dpToPx(getContext(), relativeOffset);
        return (T) this;
    }


    public <T extends BubbleDialog> T setBubbleLayout(BubbleLayout bl)
    {
        this.mBubbleLayout = bl;
        return (T) this;
    }


    public <T extends BubbleDialog> T setTransParentBackground()
    {
        Window window = getWindow();
        if (window == null) return (T) this;
        window.setDimAmount(0);
        return (T) this;
    }
}
