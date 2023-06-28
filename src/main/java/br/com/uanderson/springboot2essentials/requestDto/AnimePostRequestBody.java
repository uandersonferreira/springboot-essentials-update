package br.com.uanderson.springboot2essentials.requestDto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AnimePostRequestBody {
        //Classe funciona como um DTO

        @NotEmpty(message = "The anime name cannot be empty")// pega campos nulos também
        private String name;
}

/*
Algumas validações disponiveis:

@NotNull - 	It determines that the value can't be null. ( != null )
@Min 	 - It determines that the number must be equal or greater than the specified value. ( >= )
@Max 	 - It determines that the number must be equal or less than the specified value. ( <= )
@Size(min,max)   – Checks if the annotated element’s size is between min value and max value provided (inclusive).
@Pattern - It determines that the sequence follows the specified regular expression. ( Expressão regular/Regex )
@Email   – Checks whether the specified character sequence/string is a valid email address. ( Email válido )
@NotBlank – Checks that the annotated character sequence/string is not null and the trimmed length is greater than 0. ( != null & length > 0 RECOMENDANDO)
@NotEmpty – Checks that the annotated element is not null and not empty.
@AssertFalse – Checks that the annotated element is false.
@AssertTrue – Checks that the annotated element is true.
@NegativeOrZero – Checks if the given element is smaller than or equal to 0. ( <= 0)
@Null – Checks that the annotated value is null. ( == null )
@Negative – Checks if the element is strictly smaller than 0. ( < 0 )
@Positive – Checks if the element is strictly greater than 0. ( > 0 )
@PositiveOrZero – Checks if the given element is greater than or equal to 0. ( >= 0 )
@CPF -  Verifica se é um cpf valido
@CNPJ -  se é um cnpj valido
 */