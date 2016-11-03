package com.github.cxt.Mynetty.jackcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends
		SimpleChannelInboundHandler<Object> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object obj)
			throws Exception {
		String jsonString = "";
		if (obj instanceof JacksonBean) {
			JacksonBean user = (JacksonBean) obj;
			
			jsonString = JacksonMapper.getInstance().writeValueAsString(user); // 对象转为json字符串
		} else {
			jsonString = JacksonMapper.getInstance().writeValueAsString(obj); // 对象转为json字符串
		}
		System.out.println("Client get msg form Server -" + jsonString);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 发送对象
		JacksonBean user = new JacksonBean();
		user.setAge(27);
		user.setName("waylau");
		List<String> sons = new ArrayList<String>();
		for (int i = 0;i <10; i++) {
			sons.add("Lucy"+i);
			sons.add("Lily"+i);
		}

		user.setSons(sons);
		Map<String, String> addrs = new HashMap<String, String>();
		for (int i = 0;i <10; i++) {
			addrs.put("001"+i, "18998366112");
			addrs.put("002"+i, "15014965012");
		}

		user.setAddrs(addrs);
		ctx.writeAndFlush(user);
	}
}
