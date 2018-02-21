"""
    Tool to check latest versions of file plugins.txt

    Example:
        $ python3 check_plugins.py
        Plugin don't exist 'scriptler'
        Need update: ssh-slaves:1.25.1

    *** This tool don't update your file ***

    To update your file copy the latest version reach and update the file.
    If have a plugin that doesn't exist you should remove it.
"""
import requests

PLUGINS_CONFIG_FILE = "../plugins.txt"
API = "https://plugins.jenkins.io/api/plugin/"


f = open(PLUGINS_CONFIG_FILE, 'r')

for line in f:

    if line[0] is not '#':

        if len(line) is not 1:
            plugins = line.split(':')

            plugin_name = plugins[0].strip()
            plugin_version = plugins[1].strip()

            req = requests.get('{0}{1}'.format(API, plugin_name))

            if req.status_code == 200:
                api_data = req.json()['gav']
                name = api_data.split(':')[1]
                version = api_data.split(':')[-1]

                if plugin_version != version:
                    print("Need update: {0}:{1}".format(plugin_name,
                                                        version))

            else:
                print("Plugin don't exist '{0}'".format(plugin_name))
