package it.mulders.futbolin.webapp.security;

import com.ibm.websphere.security.jwt.Claims;
import lombok.Builder;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
public class MockClaims implements Claims {
    private final List<String> audience;
    private final String authorizedParty;
    private final String issuer;
    private final long expiration;
    private final long issuedAt;
    private final String jwtId;
    private final long notBefore;
    private final String subject;

    private final String email;
    private final String name;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getClaim(String key, Class<T> aClass) {
        if (String.class != aClass) throw new UnsupportedOperationException();

        switch (key) {
            case "email": return (T) email;
            case "name": return (T) name;
            case "sub": return (T) subject;
        }

        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> getAllClaims() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toJsonString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        return null;
    }

    @Override
    public Object put(String key, Object value) {
        return null;
    }

    @Override
    public Object remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
    }

    @Override
    public void clear() {
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public Collection<Object> values() {
        return null;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return null;
    }
}
