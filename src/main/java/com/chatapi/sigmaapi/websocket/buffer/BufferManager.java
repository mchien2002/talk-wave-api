package com.chatapi.sigmaapi.websocket.buffer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.chatapi.sigmaapi.entity.model.SocketResponse;

public class BufferManager {
    private Queue<byte[]> bufferQueue;
    private int maxBufferSize;
    private List<SocketResponse> busStop = new ArrayList<SocketResponse>();

    public BufferManager(int maxBufferSize) {
        bufferQueue = new LinkedList<>();
        this.maxBufferSize = maxBufferSize;
    }

    public synchronized void put(byte[] data) throws InterruptedException {
        while (bufferQueue.size() >= maxBufferSize) {
            wait();
        }
        bufferQueue.offer(data);
        notifyAll();
    }

    public synchronized byte[] get() throws InterruptedException {
        while (bufferQueue.isEmpty()) {
            wait();
        }
        byte[] data = bufferQueue.poll();
        notifyAll();
        return data;
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        Object obj = objectStream.readObject();
        objectStream.close();
        return obj;
    }
}
