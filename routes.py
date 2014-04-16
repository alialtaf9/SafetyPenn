from flask import Flask, render_template, request, redirect, session, url_for
import boto
import json
import os

app = Flask(__name__)

#eventually change this to use os for the sake of security
# add requirements to a textfile
print os.environ
sdb = boto.connect_sdb(ENV['AWS_ACCESS_KEY_ID'], ENV['AWS_SECRET_ACCESS_KEY'])
domain = sdb.get_domain('members')
coordinates = []
for item in domain:
  coordinate = [item['latitude'], item['longitude'], item['message'], item['timer_id']]
  coordinates.append(coordinate)


@app.route('/')
def home():
  print "home request"
  if 'user' in session:
    return render_template('home.html', coordinates=coordinates, coordinates_json=json.dumps(coordinates))
  return redirect(url_for('login'))

@app.route('/add', methods=['POST'])
def add_notification():
  # print '******* new marker *******'
  # message = request.form['message']
  # lat = request.form['lat']
  # longitude = request.form['long']
  # timer_id = request.form['id']
  # new_coordinate = [lat, longitude, message, timer_id]
  # print new_coordinate
  # coordinates.append(new_coordinate)
  print '********************************************'
  print '********* POST REQUEST RECEIVED ************'
  print '********************************************'
  return redirect(url_for('home'))

@app.route('/remove')
def remove_notification():
  print 'remove'
  id = request.args.get('id', '')
  print id
  for coordinate in coordinates:
    print coordinate[3]
    if coordinate[3] == id:
      print 'removes'
      coordinates.remove(coordinate)
  return redirect(url_for('home'))

@app.route('/login', methods=['GET', 'POST'])
def login():
  if request.method == 'POST':
    username = request.form['username']
    password = request.form['password']
    if username == 'admin' and password == 'password':
      session['user'] = username
      return redirect(url_for('home'))
    else:
      print("login error")
      return render_template('login.html', loginError=True)
  else:
    print("load login page")
    return render_template('login.html')

@app.route('/logout')
def logout():
  # remove the username from the session if it's there
  session.pop('username', None)
  session.clear()
  return redirect('/login')

# set the secret key.  keep this really secret:
app.secret_key = 'A0Zr98j/3yX R~XHH!jmN]LWX/,?RT'

if __name__ == '__main__':
  app.run(debug=True)
