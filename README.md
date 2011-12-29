Hoptoad Notifier for Java
==================================
Provides a log4J appender that sends error messages to the airbrake application associated with the configured API key.

Example Airbrake Appender log4j.properties
-----------------------------------

    log4j.appender.HOPTOAD=hoptoad.HoptoadAppender
    log4j.appender.HOPTOAD.api_key=YOUR_API_KEY_HERE
    log4j.appender.HOPTOAD.enabled=true
    log4j.appender.HOPTOAD.env=development
    log4j.appender.HOPTOAD.endpoint=airbrake.io/notifier_api/v2/notices
    log4j.appender.HOPTOAD.secure=false


Example Hoptoad Appender log4j.xml
-----------------------------------

    <appender name="HOPTOAD" class="hoptoad.HoptoadAppender">
        <param name="api_key" value="YOUR_API_KEY_HERE"/>
        <param name="env" value="production"/>
        <param name="enabled" value="true"/>
        <param name="endpoint" value="airbrake.io/notifier_api/v2/notices"/>
        <param name="secure" value="false"/>
    </appender>

Note: `endpoint` and `secure` params are shown with default values and may be omitted.