# Network Device API
REST API solution for Network Device registration and retrieval as part of online assessment.

### API built with
* Java 8
* Spring Boot 2.5.5
* Build tool: Maven (Wrapper included)

### API endpoints:
* GET: **/device**  
Retrieves all registered devices, sorted by device type (Gateway > Switch > Access Point)
* GET **/device/findByMacAddress**  
Retrieves device by MAC Address. Returns HTTP status 404, if no such device found.  
Query parameters:  
  * macAddress : MAC address of the searchable device  
Example: **{url}/device/findByMacAddress?macAddress=fa-c0-ee-da-38-18**
* POST: **/device/register**  
Registers a new device to the network deployment.  
Query parameters:  
  * deviceType : Device Type. Possible values = [GATEWAY, SWITCH, ACCESS_POINT]
  * macAddress : MAC Address of the new device.
  * (optional) uplinkMacAddress : MAC address of the uplink device (if present)  
    Examples:   
    **{url}/device/register?deviceType=SWITCH&macAddress=fa-c0-ee-da-38-18**  
    **{url}/device/register?deviceType=GATEWAY&macAddress=a4:50:6d:1d:27:f4&uplinkMacAddress=fa-c0-ee-da-38-18**
* GET: **/device/topology**  
Retrieves device topology of all registered devices OR from a specific device (if specified in the query parameters).  
If the specified device is not registered in the network deployment, then HTTP status 404 is returned.  
Query parameters:  
  * (optional) macAddress : MAC address of the root device (root of the topology)  
Examples:  
    **{url}/device/topology**  
    **{url}/device/topology?macAddress=fa-c0-ee-da-38-18**
