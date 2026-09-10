
public class ConnectionWrapper implements Connection{
    private final ConnectionPoolManager connectionPoolManager;
    private final SqlConnection physicalConnection;

    public ConnectionWrapper(SqlConnection physicalConnection, ConnectionPoolManager connectionPoolManager){
        this.connectionPoolManager = connectionPoolManager;
        this.physicalConnection = physicalConnection;
    }

    @Override 
    public void connect() throws CouldNotConnectException{
        this.physicalConnection.connect();
    }

    @Override 
    public void close(){
        this.connectionPoolManager.close(this);
        this.physicalConnection.setState(ConnectionState.IDLE);
    }
}