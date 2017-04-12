There are 6 profiles to choose a browser to test on:

* ``browser-firefox``
    
    To run tests on Mozilla Firefox. If its binary is installed in the usual place, no additional information is         required.

* ``browser-chrome``
    
    To run tests on Google Chrome. Need to pass a ``-Darq.extension.webdriver.chromeDriverBinary`` property
    pointing to a ``chromedriver`` binary.

* ``browser-ie``
    
    To run tests on Internet Explorer. Need to pass a ``-Darq.extension.webdriver.ieDriverBinary`` property
    pointing to a ``IEDriverServer.exe``.

* ``browser-safari``
    
    To run tests on Safari. If its binary is installed in the usual place, no additional information is required.

* ``browser-opera``
    
    To run tests on Opera. Need to pass a ``-Darq.extension.webdriver.opera.binary`` property pointing to a Opera        executable.

* ``browser-phantomjs``
    
    To run tests on headless browser PhantomJS. If you do not specify the path of phantomjs binary via 
    ``-Dphantomjs.binary.path`` property, it will be downloaded automatically.