package com.solution.catterpillars.data.remort;

/**
 * Author : Arvindo Mondal
 * Email : arvindomondal@gmail.com
 * Created on : 12/14/2018
 * Company : Roundpay Techno Media OPC Pvt Ltd
 * Designation : Programmer and Developer
 */
interface GsonResponseListener {

    /**
     * @param type response object class type
     * @param responseBody parsed response
     */
    void onCacheableResponse(Class type, Object responseBody);
}
