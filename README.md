# oneM2M Watson IoT interworking
oneM2M Watson IoT Platform interworking

## Clone and build from source
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

## Configure the interworking
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

## Configure the templates
The templates should be added to the folder "Templates"
The templates "template0" and "template1" are provided as examples

#### template0 file
```json
{
	"deviceType":"{{mySensorType}}",
	"deviceId":"{{mySensorId}}",
	"event":{{mySensorData}}
}
```
#### template1 file
```json
{
	"deviceType":"{{myActuatorType}}",
	"deviceId":"{{myActuatorId}}",
	"event":{{myActuatorData}}
}
```
