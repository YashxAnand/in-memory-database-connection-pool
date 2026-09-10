
public class SqlConnection implements  Connection{
    private final String url;
    private final String database;
    private final int port;
    private final String username;
    private final String password;
    private ConnectionState state;

    private SqlConnection(SqlConnectionBuilder builder){
        this.url = builder.url;
        this.database = builder.database;
        this.port = builder.port;
        this.username = builder.username;
        this.password = builder.password;
        this.state = ConnectionState.IDLE;
    }

    public static class SqlConnectionBuilder{
        private String url;
        private String database;
        private int port;
        private String username;
        private String password; 

        public SqlConnectionBuilder url(String url){
            this.url = url;
            return this;
        }

        public SqlConnectionBuilder database(String database){
            this.database = database;
            return this;
        }

        public SqlConnectionBuilder port(int port){
            this.port = port;
            return this;
        }

        public SqlConnectionBuilder username(String username){
            this.username = username;
            return this;
        }

        public SqlConnectionBuilder password(String password){
            this.password = password;
            return this;
        }

        public SqlConnection build(){
            return new SqlConnection(this);
        }
    }

    @Override
    public void connect() throws CouldNotConnectException{
        try{
            Thread.sleep(1000);
        }catch(Exception e){
            throw new CouldNotConnectException("Could not establish connection to "+this.url+" at port: "+this.port);
        }
    }

    @Override
    public void close(){
        this.state = ConnectionState.CLOSED;
    }
}