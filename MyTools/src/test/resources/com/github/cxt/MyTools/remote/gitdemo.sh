#yum install git expect
git config --global http.sslverify false
expect - <<EOF
spawn git clone https://github.com/caixt/dubbo.study.git
expect {
  "Username" {send "caixt\r";exp_continue}
  "Password" {send "xxxxxxxxxxx\r";exp_continue}
}
EOF