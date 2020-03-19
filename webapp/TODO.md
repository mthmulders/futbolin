* [X] Add a few "open" pages (i.e. not requiring authentication) and register them with the Google Developer Console in the OAuth consent screen:
  * [X] homepage
  * [X] privacy policy
* [ ] Add a global error handling filter that catches all kind of runtime errors and renders an error page instead.
* [ ] Use [OpenLiberty Session Caching](https://github.com/openliberty/ci.docker#session-caching).
* [ ] Improve application startup [by using Jandex index files](https://www.ibm.com/support/knowledgecenter/SSEQTP_liberty/com.ibm.websphere.wlp.doc/ae/twlp_improve_app_start.html).
* [X] Deploy the Social Login Web application on a different context path using `<socialLoginWebapp />`, see [the docs](https://openliberty.io/docs/ref/config/#socialLoginWebapp.html).