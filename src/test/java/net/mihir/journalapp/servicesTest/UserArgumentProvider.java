package net.mihir.journalapp.servicesTest;

import net.mihir.journalapp.entity.User;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class UserArgumentProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of(User.builder().userName("mihir2").password("1234").build()),
                Arguments.of(User.builder().userName("chotu").password("abcd").build())
        );
    }
}
