# snrl-service

Save Now, Read Later - Quick and easy transfer of stuff between devices

## Basic Structure

- This: a REST service to provide content and device info to clients
- Clients: apps, web - take advantage of the RESTful API to display data nicely for users
- Planned Clients: Android, UWP/GTK, maybe iOS

## How It Should Work

### General Functionality

1. User sees something they want to check later on another device (image, link, text, etc)
2. User shares it with snrl on their device
3. User goes to snrl on another device on their own time and checks it.

### Adding Devices

1. From an existing device, client displays something for the user. (in current implementation, a pair of randomly 
generated codes)
2. User types in the codes on snrl on the device they want to add.
3. Device is added.

## Roadmap

0. Fix some bugs
1. Transition HTTP query strings into headers
2. Implement error handling for bad requests
3. Research secure methods of handling user/device data (HTTPS and secure headers)
4. Develop a design for a user feed + "posts" on that feed
5. Develop clients

## API

### Users

All users are given a unique ID when they are created. This is stored along with a non-unique display name and the date 
they were created. The current implementation with MongoDB allows for flexible feature adding; i.e in the future, snrl 
can add fields for email, passwordhash, etc.

Structure of a user:

`{`

    `"id": "5ae7d7aeaf05172286591350",`

    `"name": "Frank Poon",`

    `"dateCreated": "2018-04-20T18:25:43.511Z"`

`}`

**"/users/all"** - Accepts headers: none

Example: `curl <host>/users/all`

Mainly used for administrative purposes. Returns a list of all users registered in snrl.

**"/users/get"** - Accepts headers: **id - user id**

Example: 

Returns information about the given user.

**"/users/add"** - Accepts headers: **name - user's display name**, **deviceName - device display name**

Example:  

Registers a new user along with their first device, and returns information about the new user.

**"/users/update"** - Accepts headers: **id - user id**, **name - user's new display name**

Example: 

Changes a user's display name. Can be scalable with more headers in the future if snrl requires more user information.

**"/users/remove"** - Accepts headers: **id - user id**

Example: 

Mainly used for administrative purposes. Removes a user.

### Devices - Existing

Devices are tied to users through a userId field that is supplied when they are created. They also have their unique 
device IDs, non-unique display names, and date created. Like Users, they are flexible in their implementations and new
 fields can be created at any time. This section deals with existing devices that have already been added; adding new
 devices takes a separate process detailed in the next section.
 
 Structure of a device:
 
 `{`
  
    `"id": "5ae91b58af0517121f12e5b6",`
 
    `"name": "Frank Poon",`
 
    `"userId": "5ae7d7aeaf05172286591350"`
 
    `"dateCreated": "2018-04-20T18:25:43.511Z"`
 
 `}`

**"/devices/all** - Accepts headers: none

Example: `curl <host>/devices/all`

Mainly used for administrative purposes. Returns a list of all devices registered in snrl, belonging to all users.

**"/devices/get** - Accepts headers: **userId - user id**

Example: 

Returns a list of all the devices belonging to the given user.

Information includes: id, name, userId, dateCreated

**"/devices/update"** - Accepts headers: **id - device id**, **name - new device display name**

Example: 

Changes the device with the given id to the given display name, and returns it. 

**"/devices/remove"** - Accepts headers: **id - device id**

Removes the device with the given id, and returns it.

### Devices - Authenticating/Adding New Devices

Devices are authenticated and added in a two step process. The first step involves generating and issuing a device
token, which includes a 6 digit numeric pin, and a 4 digit authentication code, along with the associated user ID. 
These two codes are displayed to the user, who enters them in the new device. The server then authenticates the new
device, and, if successful, creates a record of it with its unique device ID and a blank name.

Structure of a device token:

 `{`
  
    `"pin": "603182",`
 
    `"authenticationCode": "GQXY",`
 
    `"userId": "5ae7d7aeaf05172286591350"`
 
    `"dateCreated": "2018-04-20T18:25:43.511Z"`
 
 `}`


**"/devices/add"** - Accepts headers: **userId - user id**

Example: 

Takes in the given user id and returns a device token, which is displayed by the client for the user to self 
authenticate through another client.

**"/devices/authenticate** - Accepts headers: **pin - pin (6 digit numeric)**, 
**authenticationCode - authentication code (4 digits alphabetical)**

Example: 

Attempts to authenticate the user with the given pin and authentication code from a device token on another client. If
successful, creates a device with a unique ID and a blank name, which can then be updated through another API request.

### Content - coming soon!