from flask import Flask, render_template, request, redirect, session
import mysql.connector
import logging

logging.basicConfig(level=logging.DEBUG)
logger = logging.getLogger(__name__)

app = Flask(__name__)
app.secret_key = 'fasdgfdgdfg'

def get_db_connection():
    return mysql.connector.connect(user='root', password='root', host='mysql-service', database='student_database')

@app.route('/')
def home():
   return render_template('home.html')

@app.route('/addstudent')
def new_student():
   return render_template('add_student.html')

@app.route('/addrec',methods = ['POST', 'GET'])
def addrec():
   if request.method == 'POST':
      try:
         name = request.form['name']
         addr = request.form['address']
         city = request.form['city']
         pin = request.form['pin']
         
         con = get_db_connection()
         cur = con.cursor()
         cur.execute("INSERT INTO students (name,addr,city,pin) VALUES (%s,%s,%s,%s)",(name,addr,city,pin))
         con.commit()
         msg = "Record successfully added!"
      except:
         con.rollback()
         msg = "error in insert operation"
      finally:
         con.close()
         return render_template("list.html",msg = msg)

@app.route('/list')
def list():
   con = get_db_connection()
   cur = con.cursor()
   cur.execute("SELECT * FROM students")
   students = cur.fetchall()
   logger.debug(students)
   cur.close()
   con.close()
   return render_template("list.html", students = students)

if __name__ == '__main__':
   app.run(debug = True, host='0.0.0.0', port=5000)