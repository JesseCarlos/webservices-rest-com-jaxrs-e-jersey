package br.com.alura.loja.resource;

import br.com.alura.loja.dao.CarrinhoDAO;
import br.com.alura.loja.modelo.Carrinho;
import br.com.alura.loja.modelo.Produto;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("carrinhos")
public class CarrinhoResource {

    private CarrinhoDAO carrinhoDAO = new CarrinhoDAO();

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Carrinho busca(@PathParam("id") long id) {
        return carrinhoDAO.busca(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_XML)
    public Response adiciona(Carrinho carrinho) {
        carrinhoDAO.adiciona(carrinho);

        final URI location = URI.create("/carrinhos/" + carrinho.getId());
        return Response.created(location).build();
    }

    @Path("{id}/produtos/{produtoId}")
    @DELETE
    public Response remove(@PathParam("id") long id, @PathParam("produtoId") long produtoId) {
        carrinhoDAO.busca(id).remove(produtoId);
        return Response.ok().build();
    }

    @Path("{id}/produtos/{produtoId}/quantidade")
    @PUT
    public Response alteraProduto(Produto produto, @PathParam("id") long id, @PathParam("produtoId") long produtoId) {
        Carrinho carrinho = carrinhoDAO.busca(id);
        carrinho.trocaQuantidade(produto);
        return Response.ok().build();
    }
}
