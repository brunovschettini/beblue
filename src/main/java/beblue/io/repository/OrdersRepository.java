package beblue.io.repository;

import beblue.io.model.Orders;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Override
    Orders getOne(Long id);
}
