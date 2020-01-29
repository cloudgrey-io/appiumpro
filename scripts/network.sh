#!/bin/bash 
set -e

start_stop=$1
mode=$2
modes=("100% Loss" 3G DSL Edge LTE "Very Bad Network" "Wi-Fi" "Wi-Fi 802.11ac")

if [[ $start_stop == "stop" ]]; then
    echo "Resetting network conditioning..."
    sudo dnctl -q flush
    sudo pfctl -f /etc/pf.conf 
    echo "done"
    exit 0
fi

if [[ $start_stop == "start" ]]; then
    if [[ $# != "2" ]]; then
        echo "All required params not present; should be like './network.sh start mode'"
        exit 1
    fi

    for m in "${modes[@]}"; do
      if [ "$m" == "$mode" ]; then
          yes=1
          break
      fi
    done
    if [[ "$yes" != "1" ]]; then
        echo "Mode '$mode' is not a valid mode. Should be one of:"
        echo "${modes[@]}"
        exit 1
    fi

    if [[ "$mode" == "100% Loss" ]]; then
        down_bandwidth="0Kbit/s"
        down_packets_dropped="1.0"
        down_delay="0"
        up_bandwidth="0Kbit/s"
        up_packets_dropped="1.0"
        up_delay="0"
    elif [[ "$mode" == "3G" ]]; then
        down_bandwidth="780Kbit/s"
        down_packets_dropped="0.0"
        down_delay="100"
        up_bandwidth="330Kbit/s"
        up_packets_dropped="0.0"
        up_delay="100"
    elif [[ "$mode" == "DSL" ]]; then
        down_bandwidth="2Mbit/s"
        down_packets_dropped="0.0"
        down_delay="5"
        up_bandwidth="256Kbit/s"
        up_packets_dropped="0.0"
        up_delay="5"
    elif [[ "$mode" == "Edge" ]]; then
        down_bandwidth="240Kbit/s"
        down_packets_dropped="0.0"
        down_delay="400"
        up_bandwidth="200Kbit/s"
        up_packets_dropped="0.0"
        up_delay="440"
    elif [[ "$mode" == "LTE" ]]; then
        down_bandwidth="50Mbit/s"
        down_packets_dropped="0.0"
        down_delay="50"
        up_bandwidth="10Mbit/s"
        up_packets_dropped="0.0"
        up_delay="65"
    elif [[ "$mode" == "Very Bad Network" ]]; then
        down_bandwidth="1Mbit/s"
        down_packets_dropped="0.1"
        down_delay="500"
        up_bandwidth="1Mbit/s"
        up_packets_dropped="0.1"
        up_delay="500"
    elif [[ "$mode" == "Wi-Fi" ]]; then
        down_bandwidth="40Mbit/s"
        down_packets_dropped="0.0"
        down_delay="1"
        up_bandwidth="33Mbit/s"
        up_packets_dropped="0.0"
        up_delay="1"
    elif [[ "$mode" == "Wi-Fi 802.11ac" ]]; then
        down_bandwidth="250Mbit/s"
        down_packets_dropped="0.0"
        down_delay="1"
        up_bandwidth="100Mbit/s"
        up_packets_dropped="0.0"
        up_delay="1"
    fi
    echo "Starting network conditioning..."
    (cat /etc/pf.conf && echo "dummynet-anchor \"conditioning\"" && echo "anchor \"conditioning\"") | sudo pfctl -f -
    sudo dnctl pipe 1 config bw "$down_bandwidth" plr "$down_packets_dropped" delay "$down_delay"
    sudo dnctl pipe 2 config bw "$up_bandwidth" plr "$up_packets_dropped" delay "$up_delay"
    echo "dummynet out quick proto tcp from any to any pipe 1" | sudo pfctl -a conditioning -f -
    echo "dummynet in quick proto tcp from any to any pipe 2" | sudo pfctl -a conditioning -f -
    set +e
    sudo pfctl -e
    echo "done"
    exit 0
fi

echo "Need to tell us whether to 'start' or 'stop' the network conditioning"
exit 1
