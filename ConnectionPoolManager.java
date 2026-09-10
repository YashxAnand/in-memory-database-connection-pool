import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPoolManager{
    private final BlockingQueue<Connection> idleConnections;
    private final int MAX_POOL_SIZE;
    private AtomicInteger activeConnections;
    private final ReentrantLock lock;
    private final Condition condition;
    
    public ConnectionPoolManager(int maxPoolSize){
        this.idleConnections = new LinkedBlockingQueue<>();
        this.MAX_POOL_SIZE = maxPoolSize;
        this.activeConnections = new AtomicInteger(0);
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    public void close(Connection connection){
        lock.lock();
        
        try{
            idleConnections.offer(connection);
            activeConnections.decrementAndGet();
            condition.signal();
        }finally{
            lock.unlock();
        }
    }

    public Connection getConnection(){
        lock.lock();

        try{
            while(activeConnections.get() == MAX_POOL_SIZE && idleConnections.isEmpty()){
                condition.await();
            }
            
            if(!idleConnections.isEmpty())
                return idleConnections.poll();


            activeConnections.incrementAndGet();
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }finally{
            lock.unlock();
        }

        Connection sqlConnection = SqlConnection.SqlConnectionBuilder.builder("test", "test", "test").build();
        Connection connectionWrapper = new ConnectionWrapper(sqlConnection, this);
        connectionWrapper.connect();

        return connectionWrapper;
    }
}