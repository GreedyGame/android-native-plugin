#!/usr/bin/python

import requests
import os
import fnmatch
import hashlib
import sys
import getopt
import getpass

class Panel:

    auth_token = None

    def _find_files(self, directory):
        for root, dirs, files in os.walk(directory):
            for basename in files:
                filename = os.path.join(root, basename)
                yield filename

    def login(self, email, password):
        print "Logging for %s" % (email,)
        url =  "http://rest.greedygame.com/users/authenticate/auth?format=json"
        values = {'email': email, 'password': password}
        resp = requests.post(url, data=values, allow_redirects=True)

        if resp.status_code == 200:
            d = resp.json()
            self.auth_token = d['token']

    def check(self, gameId, path):
        print "Checking %s" % (path,)
        url = "https://rest.greedygame.com/v1/units/%s/check?format=json" % (gameId,)
        checksum = hashlib.md5(open(path, 'rb').read()).hexdigest()
        headers={"Authorization":"Token " + self.auth_token}

        p, name = os.path.split(path)
        
        values = {
            'checksum': checksum,
            'name': name}
        if len(p.split('greedygame/')) > 1:
            folder = p.split('greedygame/')[-1]
            values['folder'] = folder

        resp = requests.post(url, data=values, headers=headers)
        print resp.status_code
        print resp.json()
        return resp.status_code == 200
        

    def uploadAsset(self, gameId, path):
        print "Uploading %s" % (path,)
        url = "https://rest.greedygame.com/v1/units/%s?format=json" % (gameId,)
        files = {'creative': open(path, 'rb')}
        headers={"Authorization":"Token " + self.auth_token}

        p = os.path.split(path)[0]
        values={}
        if len(p.split('greedygame/')) > 1:
            folder = p.split('greedygame/')[-1]
            values={'folder' : folder}
        resp = requests.post(url, data=values, files=files, headers=headers)
        print resp.status_code
        print resp.json()
        if resp.status_code == 200:
            d = resp.json()
            print d

    def uploadGreedyAssets(self, gameId, resource_path):
        f = self._find_files(resource_path)
        for i in f:
            if self.check(gameId, i) == False:
                self.uploadAsset(gameId, i)



def main(argv):
    user = None
    password = None
    resource = None
    game_id = None

    try:
        opts, args = getopt.getopt(argv,"hg:u:r:",["game=", "username=","resource="])
    except getopt.GetoptError:
        print 'GreedyPanel.py -u <username> -g <game id> -r <resource root path>'
        sys.exit(2)

    for opt, arg in opts:

        if opt == '-h':
            print 'GreedyPanel.py -u <username> -g <game id> -r <resource root path>'
            sys.exit()
        elif opt in ("-u", "--username"):
            user = arg
        elif opt in ("-r", "--resource"):
            resource = arg
        elif opt in ("-g", "--game"):
            game_id = arg

    if not user or not resource or not game_id:
        print 'GreedyPanel.py -u <username> -g <game id> -r <resource root path>'
        sys.exit()
  

    if resource.find("greedygame") < 0:
        resource = os.path.join(resource , "greedygame")

    if os.path.exists(resource) and os.path.isdir(resource):
        password = getpass.getpass('Password:')
        p = Panel()
        p.login(user, password)
        p.uploadGreedyAssets(game_id, resource)
    else:
        print "Resources path is invalid directory, looking at %s" % (resource, )

if __name__ == "__main__":
   main(sys.argv[1:])