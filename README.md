# snrl-service
## Save Now, Read Later - Quick and easy transfer of stuff between devices

## Basic Structure
- This: a REST service to provide content and device info to clients
- Clients: apps, web - take advantage of the RESTful API to display data nicely for users

## How It Should Work
1. User sees something they want to check later on another device (image, link, text, etc)
2. User shares it with snrl on their device
3. User goes to snrl on another device on their own time and checks it.

## Adding devices
1. From an existing device, client displays a randomly generated code for the user. (actually a pair of randomly generated numbers)
2. User types in the code on snrl on the device they want to add.
3. Device is added.
