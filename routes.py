from flask import Flask, render_template, request, redirect, session, url_for
import boto
import json
import os

app = Flask(__name__)

#eventually change this to use os for the sake of security
# add requirements to a textfile
sdb = boto.connect_sdb(os.environ['AWS_ACCESS_KEY_ID'], os.environ['AWS_SECRET_ACCESS_KEY'])
members = sdb.get_domain('members')
notifications = sdb.get_domain('notifications')
members_list = {}
notifications_list = []

for item in members:
  members_list[item.name] = {'name': item['name'], 'gender': item['gender'], 'height' : item['height'], 'weight' : item['weight'], 'eye_color' : item['eye_color'], 'hair_color' : item['hair_color'], 'picture' : item['picture']}

for item in notifications:
  notification = [item['latitude'], item['longitude'], members_list[item.name], item['message'], item.name]
  notifications_list.append(notification)

#check if logged in, redirect to homepage
@app.route('/')
def home():
  if 'user' in session:
    return render_template('home.html', coordinates=notifications_list, coordinates_json=json.dumps(notifications_list))
  return redirect(url_for('login'))

#when a user registers he is added to the db
@app.route('/addmember', methods=['POST'])
def add_member():
  new_member = members.new_item(request.form['email'])
  new_member['name'] = request.form['name']
  new_member['gender'] = request.form['gender']
  new_member['height'] = request.form['height']
  new_member['weight'] = request.form['weight']
  new_member['eye_color'] = request.form['eye_color']
  new_member['hair_color'] = request.form['hair_color']
  new_member['picture'] = request.form['picture']
  new_member.save()
  return redirect(url_for('home'))

#when a timer goes off a notification is added to the database
@app.route('/addnotification', methods=['POST'])
def add_notification():
  new_notification = notifications.new_item(request.form['email'])
  new_notification['latitude'] = request.form['latitude']
  new_notification['longitude'] = request.form['longitude']
  new_notification['message'] = "The timer has gone off! Please send help."
  new_notification.save()
  notification = [new_notification['latitude'], new_notification['longitude'], members_list[request.form['email']], new_notification['message'], request.form['email']]
  notifications_list.append(notification)
  return redirect(url_for('home'))

@app.route('/remove')
def remove_notification():
  print 'remove'
  id = request.args.get('id', '')
  print id
  for notification in notifications_list:
    if notification[4] == id:
      notifications_list.remove(notification)
  notifications.delete_item(notifications.get_item(id))
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
      return render_template('login.html', loginError=True)
  else:
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
