* [ ] Add a few "open" pages (i.e. not requiring authentication) and register them with the Google Developer Console in the OAuth consent screen:
  * [ ] homepage
  * [ ] privacy policy
* [ ] The `googleOidcClient` has a `AuthorizationGenerator` that always assigns the same role to _any_ user.
This is probably not what we want. 
* [ ] Add a global error handling filter that catches all kind of runtime errors and renders an error page instead.