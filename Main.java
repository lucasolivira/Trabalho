import java.sql.*;

public class Main {
    // Configurações do banco de dados
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/money";
    static final String USER = "money";
    static final String PASS = "money123";

    // Conexão com o banco de dados
    static Connection conn = null;
    static Statement stmt = null;

    public static void main(String[] args) {
        try {
            // Registra o driver JDBC
            Class.forName(JDBC_DRIVER);

            // Estabelece a conexão com o banco de dados
            System.out.println("Conectando ao banco de dados...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            // Criação da tabela orcamento se não existir
            createTable();

            // Testes
            testCRUD();

            // Fecha a conexão com o banco de dados
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Bloco finally para fechar os recursos
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

    // Método para criar a tabela orcamento
    static void createTable() throws SQLException {
        stmt = conn.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS orcamento " +
                "(id INTEGER NOT NULL AUTO_INCREMENT, " +
                " valorOrcamento DOUBLE, " +
                " qtdItens INTEGER, " +
                " descontoOrcamento DOUBLE, " +
                " PRIMARY KEY ( id ))";
        stmt.executeUpdate(sql);
    }

    // Método para inserir um novo registro
    static void create(double valorOrcamento, int qtdItens, double descontoOrcamento) throws SQLException, InterruptedException {
        String sql = "INSERT INTO orcamento (valorOrcamento, qtdItens, descontoOrcamento) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setDouble(1, valorOrcamento);
        pstmt.setInt(2, qtdItens);
        pstmt.setDouble(3, descontoOrcamento);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Método para recuperar todos os registros
    static void readAll() throws SQLException, InterruptedException {
        String sql = "SELECT * FROM orcamento";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        // Simula um delay de 3 segundos no primeiro GET
        Thread.sleep(3000);

        // Processa os resultados
        while (rs.next()) {
            System.out.println("ID: " + rs.getInt("id") +
                    ", Valor: " + rs.getDouble("valorOrcamento") +
                    ", Itens: " + rs.getInt("qtdItens") +
                    ", Desconto: " + rs.getDouble("descontoOrcamento"));
        }

        rs.close();
        pstmt.close();
    }

    // Método para atualizar um registro existente
    static void update(int id, double valorOrcamento, int qtdItens, double descontoOrcamento) throws SQLException {
        String sql = "UPDATE orcamento SET valorOrcamento=?, qtdItens=?, descontoOrcamento=? WHERE id=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setDouble(1, valorOrcamento);
        pstmt.setInt(2, qtdItens);
        pstmt.setDouble(3, descontoOrcamento);
        pstmt.setInt(4, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Método para deletar um registro existente
    static void delete(int id) throws SQLException {
        String sql = "DELETE FROM orcamento WHERE id=?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        pstmt.close();
    }

    // Método para testar as operações CRUD
    static void testCRUD() throws SQLException, InterruptedException {
        // Criação de registros de teste
        create(1000.0, 5, 50.0);
        create(2000.0, 3, 30.0);

        // Recupera todos os registros
        System.out.println("Todos os registros:");
        readAll();

        // Atualiza o primeiro registro
        update(1, 1500.0, 8, 75.0);

        // Recupera todos os registros após a atualização
        System.out.println("Todos os registros após a atualização:");
        readAll();

        // Deleta o segundo registro
        delete(2);

        // Recupera todos os registros após a exclusão
        System.out.println("Todos os registros após a exclusão:");
        readAll();
    }
}