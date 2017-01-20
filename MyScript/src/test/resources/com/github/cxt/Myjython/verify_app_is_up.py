import requests
import time
message = 'Application is not up after ' + str(attempts) + ' attempts to ping.'
if ssl == '1':
    url = "https://" + host + ":" + port
else:
    url = "http://" + host + ":" + port
count = 0
return_result = False
while (( count < int(attempts) ) and ( not return_result )):
    try:
        result = requests.get(url, timeout=int(attempt_timeout))
    except Exception as e:
        count = count + 1
        time.sleep(int(time_to_sleep))
    else:
        code = result.status_code
        count = int(attempts)
        if code == 200 :
            return_result = True
            message = "Application is up"