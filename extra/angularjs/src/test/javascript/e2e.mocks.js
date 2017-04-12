parent.mocks = ['ngMockE2E'];
parent.mocks.api = {
    delete_note: {},
    get_notes: {},
    post_note: {}
};
parent.mocks.initializeMocks = function ($httpBackend, $log)
{
    var DELETE = "DELETE";
    var POST = "POST";
    var GET = "GET";

    function setup(method, url, config)
    {
        $httpBackend.when(method, url).respond(function (requestMethod, requestUrl, notSureWhatItIs, headers)
        {
            $log.debug(requestMethod + " " + requestUrl);
            config.triggered = true;
            var response = config.response || {};
            if (config.serverLogic) {
                response = config.serverLogic(requestMethod, requestUrl, notSureWhatItIs, headers);
            }
            return [response.code || 200, response.data];
        });
    }

    setup(DELETE, /\/rest\/note\/\d+/, mocks.api.delete_note);
    setup(GET, "/rest/note", mocks.api.get_notes);
    setup(POST, "/rest/note", mocks.api.post_note);

    $httpBackend.whenGET(/.*\.html/).passThrough();
};

angular.scenario.dsl('mockApi', function ()
{
    var api = parent.mocks.api;
    return function (itemName)
    {
        var chain = {};
        chain.reset = function ()
        {
            function reset(config)
            {
                delete config.response;
                delete config.serverLogic;
                config.triggered = false;
            }

            if (parent.mocks.api.hasOwnProperty(itemName)) {
                return this.addFuture('reset mock ' + itemName, function (done)
                {
                    reset(api[itemName]);
                    done(null);
                });
            } else {
                return this.addFuture('reset all mocks', function (done)
                {
                    for (var key in api) {
                        if (api.hasOwnProperty(key)) {
                            reset(api[key]);
                        }
                    }
                    done(null);
                });
            }
        };
        chain.response = function (value)
        {
            if (undefined == value) {
                if (!api.hasOwnProperty(itemName)) {
                    throw new Error("Unknown API item: " + name);
                } else {
                    return this.addFuture("get response for " + itemName, function (done)
                    {
                        done(null, api[itemName].response);
                    });
                }
            }
            var stringifiedValue;
            try {
                stringifiedValue = JSON.stringify(value);
            } catch (e) {
                stringifiedValue = "" + value;
            }
            return this.addFuture("set response for " + itemName + ":" + stringifiedValue, function (done)
            {
                api[itemName].response = value;
                done(null);
            });
        };
        chain.serverLogic = function (value)
        {
            if (!api.hasOwnProperty(itemName)) {
                throw new Error("Unknown API item: " + name);
            }
            return this.addFuture("set server logic for " + itemName, function (done)
            {
                api[itemName].serverLogic = value;
                done(null);
            });
        };
        chain.triggered = function (value)
        {
            if (undefined == value) {
                if (!api.hasOwnProperty(itemName)) {
                    throw new Error("Unknown API item: " + itemName);
                } else {
                    return this.addFuture("get triggered flag for " + itemName, function (done)
                    {
                        done(null, api[itemName].triggered);
                    });
                }
            }
            return this.addFuture("set triggered flag for " + itemName, function (done)
            {
                api[itemName].triggered = value;
                done(null);
            });
        };
        return chain;
    };
});

angular.scenario.dsl('cookies', function ()
{
    var chain = {};
    chain.clear = function (name)
    {
        return this.addFutureAction('clear cookie ' + name, function ($window, $document, done)
        {
            var injector = $window.angular.element($window.document.body).inheritedData('$injector');
            var cookies = injector.get('$cookies');
            var root = injector.get('$rootScope');
            delete cookies[name];
            root.$apply(); // forcibly flush the cookie changes
            done();
        });
    };
    chain.clearAll = function ()
    {
        return this.addFutureAction('clear all cookies', function ($window, $document, done)
        {
            var injector = $window.angular.element($window.document.body).inheritedData('$injector');
            var cookies = injector.get('$cookies');
            var root = injector.get('$rootScope');
            for (var name in cookies) {
                if (cookies.hasOwnProperty(name)) {
                    delete cookies[name];
                }
            }
            root.$apply(); // forcibly flush the cookie changes
            done();
        });
    };
    return function ()
    {
        return chain;
    }
});
