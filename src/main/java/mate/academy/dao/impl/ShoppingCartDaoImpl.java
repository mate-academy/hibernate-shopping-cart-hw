package mate.academy.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import mate.academy.dao.ShoppingCartDao;
import mate.academy.model.ShoppingCart;
import mate.academy.model.User;

public class ShoppingCartDaoImpl implements ShoppingCartDao {

    private final Connection connection;

    public ShoppingCartDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ShoppingCart add(ShoppingCart shoppingCart) {
        try (PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO shopping_cart (user_id) VALUES (?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, shoppingCart.getUser().getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        shoppingCart.setId(generatedKeys.getLong(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert shopping cart", e);
        }
        return shoppingCart;
    }

    @Override
    public Optional<ShoppingCart> getByUser(User user) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM shopping_cart WHERE user_id =?")) {
            statement.setLong(1, user.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ShoppingCart shoppingCart = new ShoppingCart();
                shoppingCart.setId(resultSet.getLong("id"));
                shoppingCart.setUser(user); // Assuming you have a setter for User
                return Optional.of(shoppingCart);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find shopping cart by user", e);
        }
        return Optional.empty();
    }

    @Override
    public void update(ShoppingCart shoppingCart) {
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE shopping_cart SET user_id =? WHERE id =?")) {
            statement.setLong(1, shoppingCart.getUser().getId());
            statement.setLong(2, shoppingCart.getId());
            int rowsAffected = statement.executeUpdate();
            if (rowsAffected == 0) {
                throw new RuntimeException("Failed to update shopping cart");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update shopping cart", e);
        }
    }
}
