#!/usr/bin/env sh
cd cxt/test
/usr/bin/expect - <<EOF
spawn sh test1.sh
expect {
  "请输入字符串:"
  {
    send "abcedfg\r"
    exp_continue
  }
}
EOF
