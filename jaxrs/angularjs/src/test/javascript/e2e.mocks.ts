/**
 * This file might be used by your IDE (i.e. IntelliJ Idea) for code completion.
 */
declare module e2e_mocks {
    export interface Future {
    }
    export interface MockApi {
        reset():Future;
        response(value?:any):Future;
        serverLogic(value:Function):Future;
        triggered(value?:bool):Future;
    }
    export interface Cookies {
        clear(name:String):Future;
        clearAll():Future;
    }
}
declare function mockApi(item:String):e2e_mocks.MockApi;
declare function mockApi():e2e_mocks.MockApi;
declare function cookies():e2e_mocks.Cookies;
