
interface Connection{
    void connect() throws CouldNotConnectException;
    void close();
}