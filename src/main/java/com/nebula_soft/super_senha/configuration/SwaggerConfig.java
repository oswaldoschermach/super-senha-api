package com.nebula_soft.super_senha.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Super Senha API - Jogo de Adivinhação de Palavras")
                        .version("1.0")
                        .description("""
                            <h1>🚀 Super Senha - API do Jogo</h1>
                            
                            <h2>🎮 Sobre o Jogo</h2>
                            <p>O <strong>Super Senha</strong> é um jogo de adivinhação de palavras onde:</p>
                            <ul>
                                <li>Os jogadores competem para descobrir palavras secretas</li>
                                <li>Cada palavra tem um nível de dificuldade (Fácil, Médio, Difícil)</li>
                                <li>O sistema fornece dicas e feedback durante o jogo</li>
                                <li>Os jogadores acumulam pontos conforme acertam as palavras</li>
                            </ul>
                            
                            <h2>🛠️ Funcionalidades Principais</h2>
                            <div style="background:#f5f5f5;padding:15px;border-radius:5px">
                                <h3>👥 Gestão de Jogadores</h3>
                                <ul>
                                    <li>Cadastro e autenticação de jogadores</li>
                                    <li>Perfis com histórico de partidas</li>
                                    <li>Ranking de pontuações</li>
                                </ul>
                                <h3>🎲 Gestão de Partidas</h3>
                                <ul>
                                    <li>Criação de salas de jogo (Game Rooms)</li>
                                    <li>Controle de rodadas e turnos</li>
                                    <li>Distribuição de palavras aleatórias por dificuldade</li>
                                </ul>
                                <h3>📊 Dicionário de Palavras</h3>
                                <ul>
                                    <li>Banco de palavras categorizadas</li>
                                    <li>Sistema de sugestões e dicas</li>
                                    <li>Administração do vocabulário do jogo</li>
                                </ul>
                            </div>
                            
                            <h2>🔧 Tecnologias Utilizadas</h2>
                            <table border="1" style="border-collapse: collapse; width: 100%">
                                <tr>
                                    <th>Tecnologia</th>
                                    <th>Descrição</th>
                                </tr>
                                <tr>
                                    <td><strong>Java 21</strong></td>
                                    <td>Linguagem principal do backend</td>
                                </tr>
                                <tr>
                                    <td><strong>Spring Boot 3</strong></td>
                                    <td>Framework para construção da API</td>
                                </tr>
                                <tr>
                                    <td><strong>PostgreSQL</strong></td>
                                    <td>Banco de dados relacional</td>
                                </tr>
                                <tr>
                                    <td><strong>JWT</strong></td>
                                    <td>Sistema de autenticação seguro</td>
                                </tr>
                                <tr>
                                    <td><strong>Swagger</strong></td>
                                    <td>Documentação interativa da API</td>
                                </tr>
                            </table>
                            
                            <h2>🔗 Fluxo Básico do Jogo</h2>
                            <ol>
                                <li>Jogador faz login/cadastro</li>
                                <li>Cria ou entra em uma sala de jogo</li>
                                <li>Sistema sorteia uma palavra secreta</li>
                                <li>Jogador faz tentativas de adivinhação</li>
                                <li>Sistema fornece feedback sobre os palpites</li>
                                <li>Ao acertar, jogador acumula pontos</li>
                                <li>Ranking é atualizado ao final da partida</li>
                            </ol>
                            <h2>🛡️ Autenticação</h2>
                            <p>Todas as requisições, exceto login/cadastro, requerem token JWT no header:</p>
                            <code>Authorization: Bearer [seu_token]</code>
                            """)
                        .termsOfService("https://nebula-soft.com/terms")
                        .contact(new Contact()
                                .name("Equipe Nebula Soft")
                                .email("suporte@nebulasoft.com")
                                .url("https://www.nebula-soft.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação Completa do Projeto")
                        .url("https://docs.nebula-soft.com/super-senha"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .name("bearerAuth")
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("Token JWT obtido no login")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .externalDocs(new ExternalDocumentation()
                        .description("Guia do Desenvolvedor Frontend")
                        .url("https://frontend-guide.nebula-soft.com/super-senha"));
    }
}