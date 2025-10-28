package net.cubizor.cubicolor.manager;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ColorSchemeContextTest {

    @Test
    void testOf_CreatesContext() {
        UUID playerId = UUID.randomUUID();

        ColorSchemeContext<UUID> context = ColorSchemeContext.of(playerId);

        assertNotNull(context);
        assertEquals(playerId, context.getContext());
    }

    @Test
    void testOf_SupportsAnyType() {
        String name = "player123";
        Integer id = 42;
        Object obj = new Object();

        ColorSchemeContext<String> ctx1 = ColorSchemeContext.of(name);
        ColorSchemeContext<Integer> ctx2 = ColorSchemeContext.of(id);
        ColorSchemeContext<Object> ctx3 = ColorSchemeContext.of(obj);

        assertEquals(name, ctx1.getContext());
        assertEquals(id, ctx2.getContext());
        assertEquals(obj, ctx3.getContext());
    }

    @Test
    void testOf_AllowsNull() {
        ColorSchemeContext<Object> context = ColorSchemeContext.of(null);

        assertNotNull(context);
        assertNull(context.getContext());
    }
}
