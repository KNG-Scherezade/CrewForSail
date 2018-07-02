import calendar
import json
import sys
from Queue import Queue
import time
from datetime import datetime





# Data storage tags
# COL =["ID","Type","Club","Boat","Crew","Deadline","Phone","Mail","BoatName","Name","Delete","Index","Specific Info"]
#        X                                   -                                          X        X

ID = 0
TYPE = 1
DEADLINE = 5
CLASSIFICATION = 10
INDEX = 11
ESCAPECHAR = 13

rows = []


# print everything in rows array for php to read
def read():
    for row in rows:
        if len(row) < 10:
            continue
        try:
            row.pop(ESCAPECHAR)
        except IndexError:
            pass
        row.pop(INDEX)
        row.pop(CLASSIFICATION)
        row.pop(ID)
        row = string(row)
        print(row)


# take data from backup using queues, splits and strips
def array(arg):
    try:
        storage = open('Storage.txt', 'r')
    except IOError:
        create = open('Storage.txt', 'w')
        storage = open('Storage.txt', 'r')
    queue = Queue()
    if len(arg) > 10:
        rows.append(arg)
    for line in storage:
        queue.put(line)
    while not queue.empty():
        entry = queue.get()
        entry = entry.rstrip()
        formatted = entry.split(" ")
        length = len(formatted)
        if length < 10:
            continue
        rows.append(formatted)
    storage.close()


# condition check for date style
# split dates into arrays accordingly
# compare earliest date,
# if its expired call set a marker for the first to modify
# check the next
# if its expired call set a marker for the first to modify
# check next
# if its expired delete the row
# 1get current date and time
# 2check type of date
# 3split the row's time up and check the first date array.
# 4check the second
# 5check the third
# 6if all of them delete the row.
# 7if only one or two need changing,
# 8modify
def subtract():
    current = datetime.now()
    year = current.year
    month = current.month
    day = current.day
    hour = current.hour
    minute = current.minute
    if hour != 24:
        switch = int(hour / 12)
    else:
        switch = 1
    hour = current.hour % 12
    for row in reversed(rows):
        truth = []
        dates = []
        if row[DEADLINE].find("~") >= 0:
            new = row[DEADLINE].split("_")
            for date in reversed(new):
                slashed = (date[:10].replace("~", "")).split("/")
                backwards = (date[10:].replace("~", "").replace("-", " ").replace(":", " ")).split(" ")
                slashed = slashed + backwards
                dates.insert(0, slashed)
                if slashed[5] == "PM":
                    slashed[5] = 1
                else:
                    slashed[5] = 0
                if year > int(slashed[0]):
                    truth.insert(0, True)
                elif year < int(slashed[0]):
                    truth.insert(0, False)
                elif month > int(slashed[1]):
                    truth.insert(0, True)
                elif month < int(slashed[1]):
                    truth.insert(0, False)
                elif day > int(slashed[2]):
                    truth.insert(0, True)
                elif day < int(slashed[2]):
                    truth.insert(0, False)
                elif switch > int(slashed[5]):
                    truth.insert(0, True)
                elif switch < int(slashed[5]):
                    truth.insert(0, False)
                elif hour > int(slashed[3]):
                    truth.insert(0, True)
                elif hour < int(slashed[3]):
                    truth.insert(0, False)
                elif minute > int(slashed[4]):
                    truth.insert(0, True)
                elif minute < int(slashed[4]):
                    truth.insert(0, False)
                else:
                    # it will be anyways
                    truth.insert(0, False)
        elif row[DEADLINE][:3].isalpha():
            new = row[DEADLINE].split(",_")
            for date in reversed(new):
                slashed = (date.replace(":", "-")).split("-")
                slashed[0] = conversion(slashed[0])
                slashed[1] = slashed[1].replace("th", "").replace("rd", "").replace("nd", "").replace("st", "")
                dates.insert(0, slashed)
                if month > int(slashed[0]):
                    truth.insert(0, True)
                elif month < int(slashed[0]):
                    truth.insert(0, False)
                elif day > int(slashed[1]):
                    truth.insert(0, True)
                elif day < int(slashed[1]):
                    truth.insert(0, False)
                elif hour > int(slashed[2]):
                    truth.insert(0, True)
                elif hour < int(slashed[2]):
                    truth.insert(0, False)
                elif minute > int(slashed[3]):
                    truth.insert(0, True)
                elif minute > int(slashed[3]):
                    truth.insert(0, False)
                else:
                    # it will be anyways
                    truth.insert(0, True)
        count = 0
        for boolean in truth:
            if boolean is True:
                del(dates[count])
                continue
            count += 1
        if len(dates) == 0:
            rows.remove(row)
        else:
            row[DEADLINE] = dateString(dates)[:-2]
        del truth
        del dates


def conversion(month):
    if month == "January":
        return 1
    elif month == "February":
        return 2
    elif month == "March":
        return 3
    elif month == "April":
        return 4
    elif month == "May":
        return 5
    elif month == "June":
        return 6
    elif month == "July":
        return 7
    elif month == "August":
        return 8
    elif month == "September":
        return 9
    elif month == "October":
        return 10
    elif month == "November":
        return 11
    elif month == "December":
        return 12
    else:
        return 0


def dateString(dates):
    days = ""
    for date in dates:
        if len(str(date[0])) == 4:
            if date[5] < 1:
                date[5] = "AM"
            else:
                date[5] = "PM"
            days += date[0] + "/" + date[1] + "/" + date[2] + "~" + date[3] + ":" + date[4] + "-" + date[5] + "~_"
        else:
            days += str(calendar.month_name[date[0]]) + "-" + date[1] + trail(int(date[1])) + "-" +\
                      date[2] + ":" + date[3] + ",_"
    return days


def trail(day):
    if day == 11 or day == 12 or day == 13:
        return "th"
    day %= 10
    if day == 1:
        return "st"
    if day == 2:
        return "nd"
    if day == 3:
        return "rd"
    else:
        return "th"


# decides what needs modifying
def change():
    for row in reversed(rows):
        if row[TYPE] == "1":
            row[TYPE] = "Need_Crew"
        elif row[TYPE] == "2":
            row[TYPE] = "Want_Boat"
        if int(row[CLASSIFICATION]) == 0:
            continue
        if int(row[CLASSIFICATION]) == 1:
            delete(row)
        elif int(row[CLASSIFICATION]) % 10 == 4:
            modify(row)


# deletes everything matching the item
def delete(item):
    for row in reversed(rows):
        if row[INDEX] == item[INDEX]:
            rows.remove(row)


# modifies everything matching the item
def modify(item):
    for row in reversed(rows):
        if row == item:
            rows.remove(row)
            continue
        if row[INDEX] == item[INDEX]:
            for x in range(len(row)):
                row[x] = item[x]
            row[CLASSIFICATION] = "0"


# writes everything in the rows array to storage
def write():
    storage = open("Storage.txt", "w")
    queue = Queue()
    for row in rows:
        queue.put(row)
    while not queue.empty():
        formatted = string(queue.get())
        print(formatted)
        storage.write(formatted)


# converts list into string
def string(row):
    statement = ""
    for entries in row:
        statement += entries + " "
    statement += "\n"
    return statement


# # # MAIN THREAD # # #
# Create a list from storage.txt,
# check for expired dates
# and then choose to read a filtered version of the txt to relay.php or modify the base file.

argv = sys.argv[1].split(" ")
#argv = ["0", "2", "PCYC", "boat3421423", "Skipper", "2018/05/30~10:15-PM", "ph", "emai", "hjgfgjhf", "na", "0", "698440.2352", "si"]
#argv = ['0', '3']
array(argv)
subtract()


if argv[1] == '3':
    # read out data
    read()
else:
    # modify rows
    change()
    # write new data
    write()


"""
["7899695620857207864", "Crew", "PCYC", "Fireball", "Skipper", "May-27th-04:00,_May-24th-04:00,_June-10th-04:26,_June-15th-04:14", "N/A", "N/A", "hhh", "N/A", "0", "-8264284442491660749[C@53da215", "df_asd_asd_"]#
["0", "2", "PCYC", "boat3421423", "Skipper", "2016/05/30~10:15-PM", "ph", "emai", "hjgfgjhf", "na", "0", "6984404.235330972", "si"]#



["7899695620857207864", "Crew", "PCYC", "Fireball", "Skipper", "May-27th_04:00,_May-24th-04:00,_June-10th-04:26,_June-15th-04:14", "N/A", "N/A", "hhh", "N/A", "1", "-8264284442491660749[C@53da215", "df_asd_asd_"]
["7899695620857207864", "1", "PCYC", "Tanzer26", "Skipper", "June_2nd_05:00", "N/A", "N/A", "hhh", "N/A", "0", "3475917884383686304[C@d113247", "N/A"]
#["0", "3", "PCYC", "boat3421423", "Skipper", "2016/4/25~12:15-PM", "ph", "emai", "hjgfgjhf", "na", "0", "6984404.235330972",	"si"]
0 1 PCYC boat Skipper 2016/4/25~8:45-AM_2016/4/27~11:15-AM ph em num na 0 1018047.3652559652 soi
# ["7899695620857207864", "3", "PCYC", "T", "Skipper", "June_2nd_05:45", "N/A", "N/A", "hhh", "N/A", "0", "3475917884383686304[C@d113247", "Nasdasd"]
["7899695620857207864", "3", "PCYC", "Tanzer26", "Skipper", "June_2nd_05:00"]#
["353646576880771197", "1", "PCYC", "Shark", "Skipper", "May_31st_00:00", "FA", "FA", "BN", "FA", "0", "-7007252005008736039%5BC%4048d3201", "FA"] #


05-24 15:46:47.242 11191-11236/com.shahzada.raceorganizer I/System.out: Received: 2  3 4 5 6 7 8 9 10 13
05-24 15:46:47.243 11191-11236/com.shahzada.raceorganizer I/System.out: Received: b c d e f g h i j m
05-24 15:46:47.244 11191-11236/com.shahzada.raceorganizer I/System.out: Received: @ #      $        %           ^         &   *   (   )   ~
05-24 15:46:47.244 11191-11236/com.shahzada.raceorganizer I/System.out: Received: 1 PCYC Tanzer26 Skipper June_2nd_05:00 N/A N/A hhh N/A N/A7899695620857207864 PCYC Tanzer26 Skipper June_2nd_05:00 N/A N/A hhh N/A 0 5608567642583682363[C@1aaf19b, N/A]
05-24 15:46:47.245 11191-11236/com.shahzada.raceorganizer I/System.out: Received:
05-24 15:46:47.245 11191-11236/com.shahzada.raceorganizer I/System.out: Received: Crew PCYC Tanzer26 Skipper June_2nd_05:00 N/A N/A hhh N/A N/A]
05-24 15:46:47.245 11191-11236/com.shahzada.raceorganizer I/System.out: Received: Boat N/A Either N/A June_10th_14:00,_May_27th_04:00 467 uiop N/A qwer oiup[
05-24 15:46:47.257 11191-11236/com.shahzada.raceorganizer I/System.out: Received:
05-24 15:46:47.257 11191-11236/com.shahzada.raceorganizer I/System.out: Received:

["0", "1", "PCYC", "hkgjl", "Skipper", "2016/5/26~12:30-PM", "N/A", "N/A", "hjkl", "N/A", "0", "9128814.214681722", "N/A"]#
"""