import re

class Domain:
	def __init__(self, domain, port):
		self.domain = domain
		self.port = port



template = """# {0.domain} running on localhost:{0.port}
server {{
    listen 0.0.0.0:80;
    server_name {0.domain};
    access_log logs/{0.domain}.log;

    # pass the request to the node.js server with the correct headers and much more can be added, see nginx config options
    location / {{
      	proxy_set_header X-Real-IP $remote_addr;
      	proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
      	proxy_set_header Host $http_host;
      	proxy_set_header X-NginX-Proxy true;

      	proxy_pass http://127.0.0.1:{0.port}/;
      	proxy_redirect off;
      
		proxy_http_version 1.1;
		proxy_set_header Upgrade $http_upgrade;
		proxy_set_header Connection "upgrade";      
    }}
}}
"""

list = []
with open('domains.txt') as f:
	for line in f:
		match = re.search('([\S]+)[\s]+([0-9]+)', line.strip())
		if(match is not None):
			pass
			list.append(Domain(match.group(1), match.group(2)))
		

for d in list:
	print template.format(d)

