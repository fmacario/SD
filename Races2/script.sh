#!/bin/bash

echo "---- Races ----"
echo "SD - P01G04"

echo "GIT CLONE"
echo "03"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws03.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script03.sh && ./script03.sh &
sleep 1

echo "01"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws01.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script01.sh && ./script01.sh &
sleep 1

echo "02"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws02.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script02.sh && ./script02.sh &
sleep 1

echo "04"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws04.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script04.sh && ./script04.sh &
sleep 1

echo "05"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws05.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script05.sh && ./script05.sh &
sleep 1

echo "06"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws06.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script06.sh && ./script06.sh &
sleep 1

echo "08"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws08.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script08.sh && ./script08.sh &
sleep 1

echo "09"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws09.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script09.sh && ./script09.sh &
sleep 1

echo "10"
sshpass -p "sys_2018" ssh -o StrictHostKeyChecking=no sd0104@l040101-ws10.ua.pt rm -rf SD/ && git clone https://github.com/bfhenriques/SD.git && cd SD/Races2 && chmod +x script10.sh && ./script10.sh &
sleep 1