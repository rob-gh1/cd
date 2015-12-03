# cd
Commerce Direct interview

"Explain how you would implement login capability for a web application that uses a user name and password. And 
describe how you would know the user is still logged in when they try to access a page. In your response please 
consider handling of security, authentication, scalability and other factors that go into creating a robust solution."

For a web application that requires login authentication without using OAuth from another source (e.g. Facebook, Twitter, Google, LinkedIn, StackOverflow, etc) is to create a database table that models a user with a username and a hashed password. A web form would allow a browser user to create a new account by entering a username and password. This web form would be SSL protected. When the form is submitted, the middle tier will validate the username and password follow guidelines and continue processing if passes, otherwise return to the browser with appropriate message to fix. Of course, some Javascript in the browser can help the experience along with pre-validation before the form is submitted.

A simple validation for username would be length>=5 and <=64, not an email address, contains alphanumeric characters, possibly with some extra punctuation characters, and auto detect for sql injection. 

A simple validation for password would be length>=8 and <=64, contains alphanumeric characters, possibly with some extra punctuation characters. Detecting for sql injection not terribly important since this password value with be hashed. 

Continuing with account creation, pass the correctly validated values to a service layer for persistence in DB but first detect if username is unique. If not unique return response with appropriate message and start process over again. If unique, then hash the password and proceed with db persistence. Create a web session and store db id for newly created account. Make use of a shared cache (Redis, Memcached) to facilitate speed.

Most common implementations use a Java filter that would verify if a session exists or not. If the session does not exist, then control is sent to a login page. Upon submitting the login information (username and password), some validation is performed, then the service layer is called to query on the username and hashed password. If exists, then create web session, and cache db id. If does not exist, return appropriate message to browser. 

Quite often when creating or querying for user account, there's a concept of authorization which can be as simple as list of zero, one, or more "roles" that give the user special privileges over navigating URLs or posting data. 

For scalability, keeping the session as light as possible is best by storing simply an identifier to be used as a key to a shared cache to obtain additional user information e.g. full name, profile details, roles, etc. This cache can be set to either expire or follow LRU so there's always some room available. If this is not possible and cookies are available, then encrypt this caching information and write it to a cookie for your website. 

