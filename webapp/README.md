# Web Application

This module contains the user-facing web application for Futbolín.

## Configuration

Configuration follows a two-step approach.
Settings can be changed in a configuration file that is packaged with the application.
This is a good place for "sane defaults".
You can override some settings using environment variables.

This are the supported configuration settings.

| Configuration file | Key | Environment variable name | Description |
| --- | --- | --- | --- |
| **oidc.properties** | `oidc.callback.url` | `OIDC_CALLBACK_URL` | Callback URL for the OpenID Connect flow |
| **oidc.properties** | `oidc.client.id` | `OIDC_CLIENT_ID` | Client identifier for the OpenID Connect integration |
| **oidc.properties** | `oidc.client.secret` | `OIDC_CLIENT_SECRET` | Client secret for the OpenID Connect integration |
