package com.github.cxt.Mynetty.snmp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.snmp4j.PDU;
import org.snmp4j.asn1.BER;
import org.snmp4j.asn1.BERInputStream;
import org.snmp4j.asn1.BEROutputStream;
import org.snmp4j.asn1.BER.MutableByte;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.Null;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;


public class SnmpServerHandler  extends SimpleChannelInboundHandler<DatagramPacket> {
	
	
	public List<VariableBinding> data = new ArrayList<>();
	
	public SnmpServerHandler(){
		//data需要排序
		data.add(new VariableBinding(new OID("1.3.6.1.2.1.1.1.0"), new OctetString("cai")));
		data.add(new VariableBinding(new OID("1.3.6.1.2.1.1.2.0"), new OctetString("xiantong")));
		data.add(new VariableBinding(new OID("1.3.6.1.2.1.1.3.0"), new OctetString("test")));
		
		data.add(new VariableBinding(new OID("1.3.6.1.4.1.1.0"), new OctetString("private1")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.1.2.0"), new OctetString("private2")));
		
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.1"), new OctetString("data11")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.2"), new OctetString("data12")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.3"), new OctetString("data13")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.4"), new OctetString("data14")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.5"), new OctetString("data15")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.6"), new OctetString("data16")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.7"), new OctetString("data17")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.8"), new OctetString("data18")));
		data.add(new VariableBinding(new OID("1.3.6.1.4.3.9"), new OctetString("data19")));
		
		data.add(new VariableBinding(new OID("1.3.6.1.6.3.13.1.0"), new OctetString("x1")));
		data.add(new VariableBinding(new OID("1.3.6.1.6.3.13.2.0"), new OctetString("x2")));
	}
	

    @Override
    public void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {    	
    	BERInputStream wholeMsg = new BERInputStream(packet.content().nioBuffer());
        MutableByte mutableByte = new MutableByte();
        int length = BER.decodeHeader(wholeMsg, mutableByte);
        int startPos = (int)wholeMsg.getPosition();

        if (mutableByte.getValue() != BER.SEQUENCE) {
          String txt = "SNMPv2c PDU must start with a SEQUENCE";
          throw new IOException(txt);
        }
        Integer32 version = new Integer32();
        version.decodeBER(wholeMsg);

        OctetString securityName = new OctetString();
        securityName.decodeBER(wholeMsg);
        PDU pdu = new PDU();
        pdu.decodeBER(wholeMsg);

        BER.checkSequenceLength(length, (int)wholeMsg.getPosition() - startPos, pdu);
        System.out.println(pdu.toString());
        pdu = handler(pdu);
        
        System.out.println(pdu.toString());
        ByteBuffer bf = out(pdu, securityName, version);
        byte[] result = bf.array();
        ctx.write(new DatagramPacket(Unpooled.copiedBuffer(result), packet.sender()));
    }
    
    
    private PDU handler(PDU pdu) throws IOException{
    	List<VariableBinding> vbs = new ArrayList<>();
    	switch (pdu.getType()) {
		case PDU.GET:{
			Map<OID, VariableBinding> map = data.stream().collect(Collectors.toMap(VariableBinding::getOid, Function.identity()));
			Vector<? extends VariableBinding> vector = pdu.getVariableBindings();
			for(int i = 0, size = vector.size(); i < size; i++){
				OID oid = vector.get(i).getOid();
				VariableBinding vb = map.get(oid);
				if(vb == null){
					if(oid.getValue().length < 5){//这个是随便造的
						vb = new VariableBinding(oid, Null.noSuchObject);
					}
					else {
						vb = new VariableBinding(oid, Null.noSuchInstance);
					}
				}
				vbs.add(vb);
			}
			break;
		}
		case PDU.GETNEXT: {
			Vector<? extends VariableBinding> vector = pdu.getVariableBindings();
			for(int i = 0, size = vector.size(); i < size; i++){
				OID index = vector.get(i).getOid();
				VariableBinding target = null;
				for(VariableBinding vb : data){
	    			if(vb.getOid().compareTo(index) > 0){
	    				target = vb;
	    				break;
	    			}
	    		}
				if(target == null){
					target = new VariableBinding(index, Null.endOfMibView);
				}
				vbs.add(target);
			}
			break;
		}
		case PDU.GETBULK : {
			Vector<? extends VariableBinding> vector = pdu.getVariableBindings();
			List<Integer> indexs = new ArrayList<>();
			int dataSize = data.size();
			for(int i = 0, size = vector.size(); i < size; i++){
				OID index = vector.get(i).getOid();
				int target = -1;
				for(int j = 0; j < dataSize; j++){
					VariableBinding vb = data.get(j);
	    			if(vb.getOid().compareTo(index) > 0){
	    				target = j;
	    				break;
	    			}
	    		}
				if(target == -1){
					target = dataSize - 1;//new VariableBinding(index, Null.endOfMibView);
				}
				indexs.add(target);
			}
			OID last = data.get(dataSize - 1).getOid();
			for(int i = 0; i < 10; i++){
				for(Integer index : indexs){
					int taget = i + index;
					if(taget >= dataSize){
						vbs.add(new VariableBinding(last, Null.endOfMibView));
					}
					else {
						vbs.add(data.get(taget));
					}
				}
			}
			break;
		}
		default:
			throw new RuntimeException(pdu.getType() + "");
		}
    	PDU result = new PDU();
    	result.setType(PDU.RESPONSE);
    	result.setRequestID(pdu.getRequestID());
    	result.setVariableBindings(vbs);
    	result.setErrorIndex(0);
    	result.setErrorStatus(0);
    	return result;
    }

    private ByteBuffer out(PDU pdu, OctetString community, Integer32 version) throws IOException{
    	BEROutputStream outgoingMessage = new BEROutputStream();
        // compute total length
        int length = pdu.getBERLength();
        length += community.getBERLength();
        length += version.getBERLength();

        ByteBuffer buf = ByteBuffer.allocate(length + BER.getBERLengthOfLength(length) + 1);
        // set the buffer of the outgoing message
        outgoingMessage.setBuffer(buf);

        // encode the message
        BER.encodeHeader(outgoingMessage, BER.SEQUENCE, length);
        version.encodeBER(outgoingMessage);

        community.encodeBER(outgoingMessage);
        pdu.encodeBER(outgoingMessage);
    	
        return buf;
    }
    
    
	@Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
    }
}
