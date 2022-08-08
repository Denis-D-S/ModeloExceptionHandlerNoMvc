
package com.spring.mongo.demo.exceptions.handler;

        import com.spring.mongo.demo.exceptions.details.ExceptionDetails;
        import com.spring.mongo.demo.exceptions.details.MethodNotAllowedDetails;
        import com.spring.mongo.demo.exceptions.details.MethodNotValidDetails;
        import com.spring.mongo.demo.exceptions.integration.APIIntegrationNotFoundException;
        import com.spring.mongo.demo.exceptions.integration.APIIntegrationServerError;
        import com.spring.mongo.demo.exceptions.notfound.NotFoundException;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.http.converter.HttpMessageNotReadableException;
        import org.springframework.validation.ObjectError;
        import org.springframework.web.HttpRequestMethodNotSupportedException;
        import org.springframework.web.bind.MethodArgumentNotValidException;
        import org.springframework.web.bind.annotation.ExceptionHandler;
        import org.springframework.web.bind.annotation.ResponseStatus;
        import org.springframework.web.bind.annotation.RestControllerAdvice;

        import java.time.LocalDateTime;
        import java.util.stream.Collectors;


@RestControllerAdvice
public class ModeloExceptionHandlerNoMvc {

    @ExceptionHandler(NotFoundException.class) //esta 'notation' chama a Classe que contém o nosso erro
    @ResponseStatus(HttpStatus.NOT_FOUND) ///erro 404 NOT FOUND
    public ExceptionDetails handlerNotFoundException(NotFoundException e) { //É NECESSÁRIO ESTE "ChangeSetPersister"??
        ExceptionDetails exceptionDetails;
        exceptionDetails = ExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("NOT_FOUND: recurso não encontrado")
                .timeStamp(LocalDateTime.now().toString())
                .details(e.getMessage())
                .developerMessage("Cheque se o ID solicitado existe no banco de dados")
                .build();
        return exceptionDetails;
    }

    /**DICA: ESTE ERRO ABAIXO É O QUE VAI EXPLODIR NA HORA DE TRATAR AQUELE ERRO QUE VEM DA API EXTERNA*/
    @ExceptionHandler(APIIntegrationNotFoundException.class) /**AQUI, NOVO ERRO QUE COLOQUEI*/
    @ResponseStatus(HttpStatus.NOT_FOUND) ///erro 404 NOT FOUND
    public ExceptionDetails handlerNotFoundException(APIIntegrationNotFoundException e) { //É NECESSÁRIO ESTE "ChangeSetPersister"??
        ExceptionDetails exceptionDetails;
        exceptionDetails = ExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("NOT_FOUND: recurso não encontrado")
                .timeStamp(LocalDateTime.now().toString())
                .details(e.getMessage())
                .developerMessage("Cheque se o ID solicitado existe na API")
                .build();
        return exceptionDetails;
    }

    @ExceptionHandler(APIIntegrationServerError.class) /**AQUI, NOVO ERRO QUE COLOQUEI PARA RESOLVER "O ERRO VINDO DA API EXTERNA"*/
    public ResponseEntity<ExceptionDetails> handlerServerErrorException(APIIntegrationServerError e) {
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .status(e.getStatus().value())
                .title("SERVER_ERROR: %s".formatted(e.getMessage()))
                .timeStamp(LocalDateTime.now().toString())
                .details(e.getMessage())
                .developerMessage("Cheque se a chamada na API está correta")
                .build();

        return ResponseEntity.status(e.getStatus()).body(exceptionDetails);
    }

    //OS 2 ERROS ABAIXO USAM O TEMPLATE "EXCEPION DETAILS.JAVA" QUE CRIAMOS

    @ExceptionHandler(HttpMessageNotReadableException.class) //esta 'notation' chama a Classe que contém o nosso erro
    @ResponseStatus(HttpStatus.BAD_REQUEST) ///erro 400 BAD REQUEST
    public ExceptionDetails handlerBadRequest(HttpMessageNotReadableException e) { //É NECESSÁRIO ESTE "ChangeSetPersister"??
        ExceptionDetails exceptionDetails;
        exceptionDetails = ExceptionDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("BAD_REQUEST: este erro 400 vem da classe 'HttpMessageNotReadableException.class'")
                .timeStamp(LocalDateTime.now().toString())
                .details("Bad Request! Este erro explodiu no método 'criar'") //daria pra botar "e.getMessage()" dentro do "details"
                .developerMessage("Cheque se a sintaxe do seu JSON está correta")
                .build();
        return exceptionDetails;
    }

    //ESTA "Exception.class" É A "MÃE" DE TODAS AS EXCEPTIONS. ELA É BEM GENÉRICA
    @ExceptionHandler(Exception.class) //esta 'notation' chama a Classe que contém o nosso erro
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) //erro 500 INTERNAL SERVER ERROR  (no POST)
    public ExceptionDetails handlerBadRequest(Exception e) {
        ExceptionDetails exceptionDetails;
        exceptionDetails = ExceptionDetails.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .title("INTERNAL_SERVER_ERRO: este é o meu erro genérico ('a mãe de todos os erros')")
                .timeStamp(LocalDateTime.now().toString())
                .details(e.getMessage())
                .developerMessage("Passamos por todos os erros e nenhum pegou. Pode ser que exista algum erro não tratado que não esteja sendo capturado")
                .build();
        return exceptionDetails;
    }

    //E AGORA OS PRÓXIMOS 2 MÉTODOS ABAIXO USAM O TEMPLATE "METHOD NOT VALID DETAILS.JAVA" QUE CRIAMOS.

    //TODO: EU ACHO QUE ESTA CLASSE ABAIXO NÃO ESTÁ FUNCIONANDO. DEVERIA APAGAR, CERTO?

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) //erro 400 BAD REQUEST (no PUT)
    public MethodNotValidDetails handlerMethodNotValid(MethodArgumentNotValidException e) {
        MethodNotValidDetails methodNotValidDetails;
        methodNotValidDetails = MethodNotValidDetails.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .title("BAD_REQUEST: este erro 400 vem da classe 'MethodArgumentNotValidException.class'")
                .timeStamp(LocalDateTime.now().toString())
                .details(e.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining(","))) //daria pra botar "e.getMessage()" dentro do "details"
                .developerMessage("Cheque se a sintaxe do seu JSON está correta")
                .build();
        return methodNotValidDetails;
    }

    //ESTE AQUI NÃO ESTÁ FUNCIONANDO, PODE DELETAR ABAIXO, O METHOD NOT ALLOWED QUE ESTÁ FUNCIONANDO É O SEGUNDO
//
//    @ExceptionHandler(HttpClientErrorException.MethodNotAllowed.class) //esta 'notation' chama a Classe que contém o nosso erro
//    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) ///erro 405 METHOD NOT ALLOWED
//    public ExceptionDetails handlerMethodNotAllowed(HttpClientErrorException.MethodNotAllowed e) { //É NECESSÁRIO ESTE "ChangeSetPersister"??
//        ExceptionDetails exceptionDetails;
//        exceptionDetails = ExceptionDetails.builder()
//                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
//                .title("METHOD NOT ALLLOOOOWWWEEDDDDD")
//                .timeStamp(LocalDateTime.now())
//                .details(e.getMessage())
//                .developerMessage("METHOD NOT ALLLOOOWWWEEEEDDDDDD")
//                .build();
//        return exceptionDetails;
//    }

    //MÉTODO INTERESSANTE PARA CRIAR UM EXCEPTION HANDLER
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED) //erro 405 METHOD NOT ALLOWED
    public ResponseEntity handlerMethodNotAllowed(HttpRequestMethodNotSupportedException e) {
//        Map<String, String> error = new HashMap<>();
//        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
//        fieldErrors.forEach(p -> error.put(p.getField(), p.getDefaultMessage()));

        return new ResponseEntity<>(MethodNotAllowedDetails.builder()
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .title("METHOD_NOT_ALLOWED: o método solicitado não é compatível com os recursos")
                .timeStamp(LocalDateTime.now())
                .details(e.getMessage()) //note que tivemos que criar um novo "details" que retorne apenas String, e não Map... se não dava problema...
                .developerMessage("Cheque se o ID ou a URL fazem sentido para o método solicitado")
                .build(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(RuntimeException.class) //esta 'notation' chama a Classe que contém o nosso erro
    @ResponseStatus(HttpStatus.NOT_FOUND) ///erro XXXX????? NOT FOUND POIS É RUNTIME EXCEPTION????
    public ExceptionDetails handlerRunTimeOutException(RuntimeException e) {
        ExceptionDetails exceptionDetails;
        exceptionDetails = ExceptionDetails.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .title("RUN_TIME_EXCEPTION: este erro 404 vem da classe 'RunTimeException.class'")
                .timeStamp(LocalDateTime.now().toString())
                .details("RunTimeException! Você provavelmente está tentando dar update numa Entidade mas passou o código errado") //daria pra botar "e.getMessage()" dentro do "details"
                .developerMessage("Cheque se o código passado está correto. ")
                .build();
        return exceptionDetails;
    }


    //TODO: verifique se este erro (sugerida pelo Ferro) funciona...

//    @ExceptionHandler(NullPointerException.class) //ESTE NULLPOINTER FOI UM EXEMPLO DE ERRO SUGERIDO PELO FERRO!
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ExceptionDetails handlerNullPointer(NullPointerException e) {
//        ExceptionDetails exceptionDetails;
//        exceptionDetails = ExceptionDetails.builder()
//                .status(HttpStatus. INTERNAL_SERVER_ERROR.value())
//                .title("Null Pointer Exception Error")
//                .timeStamp(LocalDateTime.now().toString())
//                .details("You have sent a Null where an Object was required.")
//                .developerMessage("Check for Null fields in your request.")
//                .build();
//        return exceptionDetails;
//    }


}
