# oneM2M Watson IoT interworking

## Getting Started

### Requirements
* Apache Maven 3 to build the project
* JAVA 1.7 (at least) to run the project


### Clone and build from source
Clone the project and go to the folder onem2m-watson/onem2m-watson
```sh
$ git clone https://github.com/mbenalaya/onem2m-watson.git
$ cd onem2m-watson/onem2m-watson
```
Build the project using the following command
```sh
$ mvn clean install
```
The binary, config, templates and starting script will be generated under the folder "target"

### Configure the interworking
You can configure the interworking using the file Config/config.ini

#### Watson IoT Platform parameters
```sh
id = demo@sensinov.com
Organization-ID = 8riy9e
Authentication-Method = apikey
API-Key = a-8riy9e-e2ywsxpahe
Authentication-Token = vx)RY+4MW-gqeUwkC8
Shared-Subscription = true
Clean-Session = true
```

#### oneM2M IPE parameters
The IPE AE-ID (Cae-watson in this case) must have the appropriate authorizations under the CSE. 
```sh
IpeAeId = Cae-watson
IpeAeName = ae_watson
IpeSubName = sub_watson
IpeProtocol = http
IpeIp = 127.0.0.1
IpePort = 8282
IpeContext = /watson-bridge
```
#### oneM2M CSE parameters
```sh
CseProtocol=http
CseIp= 127.0.0.1
CsePort = 8080
CseContext = /
CseId = server
CseName = server
```

#### oneM2M targeted label
```sh
label = data
```

#### oneM2M IPE templates
```sh
template.0=template0
template.1=template1
```

### Customize the mapping using templates
The templates should be added to the folder "Templates".

The following templates are provided as examples.

#### template0 file
```json
{
	"deviceType":{{mySensorType}},
	"deviceId":{{mySensorId}},
	"event":{{mySensorData}}
}
```
#### template1 file
```json
{
	"deviceType":{{myActuatorType}},
	"deviceId":{{myActuatorId}},
	"event":{{myActuatorData}}
}
```
### Start the interworking
Execute the following script to start the interworking
```sh
$ ./start.sh
```
## Demonstration

### Start a oneM2M CSE
Eclipe OM2M could be used to test the interworking.

Download the Eclipe OM2M project using the following link: https://wiki.eclipse.org/OM2M/Download

Unzip Eclipse OM2M zip file.

Enter the "in-cse" directory and run the CSE
```sh
$ ./start.sh
```
Once OM2M is running you can view its resource tree by going to the following page in a local web-browser and using the username "admin" and password "admin": http://127.0.0.1:8080/webpage

### Start the oneM2M devices simulator
A oneM2M device simulator is available on the folder "simulation".

#### Configure the simulator
Got to the folder "simulation".

You can configure the simulator using the file config.ini

##### Target CSE params
```sh
adminAeId = admin:admin
cseProtocol=http
cseIp=127.0.0.1
csePort = 8080
cseId = in-cse
cseName = in-name
reset=false
```
##### Device params
```sh
aeDeviceIdPrefix = CAE-device
aeDeviceNamePrefix = ae_device
numberOfDevice = 5
deviceSleepPeriodInMs = 1000
containerMaxNumberOfInstances = 1000
```
##### Sensor params
```sh
cntSensorPrefix = sensor
```
##### Actuator params
```sh
cntActuatorPrefix = actuator
aeDeviceProtocol= http
aeDeviceIp = 127.0.0.1
aeDevicePort = 1401
```

### Start the oneM2M Watson IoT interworking
Follow the steps explained in the "Getting Started" section to run the interworking 

### Visualize your data on Watson IoT Platform

1. Open the Watson IoT Platform in your browser using the following url:
https://internetofthings.ibmcloud.com

2. Click on SIGN IN button in on top right of the screen and use the following demo account to login:
	* Username: demo@sensinov.com
	* Password : demopasswd

3. After a successful login, click on the account menu on top/right of the screen to select an organization.

4. Select the organization "OSS (8riy9e)" to open the corresponding Watson IoT platform dashboard.

5. Click on "Devices" on the vertical menu to see the mapped devices and data.

## License
Open sourced under the Eclipse Public License 1.0.

