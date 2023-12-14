package andrei.assignment1.entities;

import org.hibernate.HibernateException;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.UUID;

@Component
public class CustomUuidGenerator implements IdentifierGenerator {
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        if (obj instanceof Device) {
            Device device = (Device) obj;
            UUID id = device.getId();
            if (id != null) {
                return id;
            } else {
                // Generate a new UUID
                return UUID.randomUUID();
            }
        }
        throw new HibernateException("Unsupported entity type: " + obj.getClass());
    }
}