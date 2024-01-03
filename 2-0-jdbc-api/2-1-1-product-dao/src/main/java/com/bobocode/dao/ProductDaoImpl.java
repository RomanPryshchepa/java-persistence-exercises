package com.bobocode.dao;

import com.bobocode.exception.DaoOperationException;
import com.bobocode.model.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;

public class ProductDaoImpl implements ProductDao {

  private final DataSource dataSource;

  public ProductDaoImpl(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void save(Product product) {
    try (Connection connection = dataSource.getConnection()) {
      Long id = 0l;
      PreparedStatement psForId = connection.prepareStatement(
          "select id from products order by id desc limit 1;");
      ResultSet rsForId = psForId.executeQuery();
      while(rsForId.next()) {
        id = rsForId.getLong(1);
      }
      PreparedStatement preparedStatement = connection.prepareStatement(
          "insert into products (id, name, producer, price, expiration_date, creation_time) values (?,?,?,?,?,?);");
      product.setId(id+1);
      preparedStatement.setLong(1, product.getId());
      preparedStatement.setString(2, product.getName());
      preparedStatement.setString(3, product.getProducer());
      preparedStatement.setBigDecimal(4, product.getPrice());
      preparedStatement.setDate(5, Date.valueOf(product.getExpirationDate()));
      preparedStatement.setDate(6, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate()));

      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw new DaoOperationException(String.format("Error saving product: %s", product), e);
    }
  }

  @Override
  public List<Product> findAll() {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(
          "select * from products;");
      var resultSet = preparedStatement.executeQuery();
      List<Product> products = new ArrayList<>();
      while (resultSet.next()) {
        Long id = resultSet.getLong("id");
        String name = resultSet.getString("name");
        String producer = resultSet.getString("producer");
        BigDecimal price = resultSet.getBigDecimal("price");
        LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
        LocalDateTime creationTime = resultSet.getDate("creation_time")
            .toLocalDate().atTime(resultSet.getTime("creation_time").toLocalTime());
        Product product = new Product(id, name, producer, price, expirationDate, creationTime);
        products.add(product);
      }
      return products;
    } catch (Exception e) {
      throw new DaoOperationException(e.getMessage(), e);
    }
  }

  @Override
  public Product findOne(Long id) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(
          "select * from products where id = ?;");
      preparedStatement.setLong(1, id);
      var resultSet = preparedStatement.executeQuery();
      resultSet.next();
      String name = resultSet.getString("name");
      String producer = resultSet.getString("producer");
      BigDecimal price = resultSet.getBigDecimal("price");
      LocalDate expirationDate = resultSet.getDate("expiration_date").toLocalDate();
      LocalDateTime creationTime = resultSet.getDate("creation_time")
          .toLocalDate().atTime(resultSet.getTime("creation_time").toLocalTime());

      return new Product(id, name, producer, price, expirationDate, creationTime);
    } catch (Exception e) {
      throw new DaoOperationException(e.getMessage(), e);
    }
  }

  @Override
  public void update(Product product) {
    try (Connection connection = dataSource.getConnection()) {
      Long id = product.getId();
      PreparedStatement preparedStatement = connection.prepareStatement(
          "update products set name = ?, producer = ?, price = ?, expiration_date = ?, creation_time = ? where id = ?;");
      product.setId(id);
      preparedStatement.setString(1, product.getName());
      preparedStatement.setString(2, product.getProducer());
      preparedStatement.setBigDecimal(3, product.getPrice());
      preparedStatement.setDate(4, Date.valueOf(product.getExpirationDate()));
      preparedStatement.setDate(5, java.sql.Date.valueOf(LocalDateTime.now().toLocalDate()));
      preparedStatement.setLong(6, product.getId());

      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw new DaoOperationException(e.getMessage(), e);
    }
  }

  @Override
  public void remove(Product product) {
    try (Connection connection = dataSource.getConnection()) {
      PreparedStatement preparedStatement = connection.prepareStatement(
          "delete from products where id = ?;");
      preparedStatement.setLong(1, product.getId());

      preparedStatement.executeUpdate();
    } catch (Exception e) {
      throw new DaoOperationException(e.getMessage(), e);
    }
  }


}
