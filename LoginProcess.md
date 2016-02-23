# Introduction #

This document includes information about logging into the WiserPath web site.

When the login occurs a few things can happen:
  1. The user is not a member and should be sent to the register page.
  1. Once registered the user can attempt to log in.
  1. The login is successful and the user is logged in, and the credential is saved.
  1. The login is unsuccessful and the user returns to non-member state, credential is not saved.
  1. The user changes his login data in preferences and the whole process is repeated.

# Details #

Currently the most important class is the services.HTTPService. This class brokers all transactions to the WiserPath website. It uses a Post object to manage the connections, get cookies and answer questions about response codes, and send data to a website. It is not specific to WiserPath. Feel free to extend it for other data types.

The Mobile app connects to the website through the login url http://wiserpath-dev.bus.ualberta.ca/user/login via the Post object. The post is formulated as
```
"name=" + credential.getUserName() + "&pass=" + credential.getPassword() + "&form_id=user_login&op=log+in" 
```
Where the post data is not url encoded.

### Tools ###
Wireshark to diagnose connection and datapassing
Firefox with LiveHTTPHeaders addon for testing post strings.