# Rocketseat Java  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-original.svg" width="40" height="40"/>
          
- Instrutora: Fernanda Kipper
- Projeto: Criar URL encurtador e redirecionar URL usando AWS Lambda

## Funcionalidades ⚙️
- Dentro do Insomnia
  
### Para criar uma URL - POST /lambda-url
  
**Payload**

```
{
	"originalUrl": "https://your-url.com",
	"expirationTime": "1732981301"
}
```

Para gerar um prazo de validade, acesse: <br>
https://www.epochconverter.com/

**Response**

```
{
	"code": "codigo-gerado-automaticamente"
}
```

### Para obter o URL e ser redirecionado - GET /lambda-url/code-generated-automatically

**Response**

Redirecionar para o URL original, com status 302

  ## Ferramentas e Tecnologias

  <div style="display: flex; justify-content:space-between;">
    <figure style="text-align:center; flex:1;">
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/amazonwebservices/amazonwebservices-original-wordmark.svg" width="40" height="40"/>
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/intellij/intellij-original.svg" width="40" height="40"/>
      <img src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/insomnia/insomnia-original.svg" width="40" height="40"/>
  </figure>
</div>
