package ufg.br.solicitadlog.api;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.query.criteria.internal.expression.function.TrimFunction;
import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.ParseException;
import ufg.br.solicitadlog.classe.Orgao;
import ufg.br.solicitadlog.classe.Usuario_Externo;
import ufg.br.solicitadlog.repository.*;


@Controller
public class Api {
	//linha abaixo com variaveis para receber os dados da api
	  String nome; String cpf1;List<String> vinculo = null;List<String> categoria = null;List<Integer> matricula = null;String situacao;Integer ano_ingersso;
	  List<String> unidade_vinculo = null;List<Integer> id_unidade = null;List<String> sigla_unidade = null;String cidade; List<String> sigla_Referencia = null;
	  String unidade;
	  String CPF;
	  Orgao orgao = new Orgao();
	  ArrayList<Orgao> listaorgao = new ArrayList<>();
	@Autowired
	private UsuarioExternoRepository usuarioExternoRepository;
	@Autowired
	private OrgaoRepository orgaoRepository;
	@Autowired
	private EspecieRepository especieRepository;
	@Autowired
	private RequisicaoRepository requisicaoRepository;
	
	@GetMapping("**/inicio")
	 public ModelAndView inicio() {
		 ModelAndView mv = new ModelAndView("acesso.html");
	     mv.addObject("nome", "");
	     mv.addObject("cpf","");
	     mv.addObject("msg","");
	     mv.addObject("siglaref", listaorgao);
	     mv.addObject("sigla", "");
	     mv.addObject("especie",especieRepository.findAll());
	     mv.addObject("requisicao",requisicaoRepository.findAll());
		 return mv;
	 }
	
	  @GetMapping("**/acesso")	
	  public ModelAndView Acesso(@RequestParam("cpf") String cpf) throws ParseException   {
		  //Linha abaixo com variaveis para receber o token de acesso
		  String access; String scope; String token_type; Integer expire;
		  

		  cpf.trim();
		  ModelAndView mv = new ModelAndView("acesso.html");
		  if (!cpf.isEmpty()) {
			  CPF=cpf;
		  JSONObject jsonobject = new JSONObject();
		  net.minidev.json.parser.JSONParser parser = new net.minidev.json.parser.JSONParser();
		  Usuario_Externo user = new Usuario_Externo();
		  HttpResponse<String> response = Unirest.post("https://data.api.ufg.br/token")
				  .header("content-type", "application/x-www-form-urlencoded")
				  .body("grant_type=client_credentials&client_id=IJzJzes8Y06GzZ52b8sT10F3uNIa&client_secret=R9jXFT1NQXZz799BYFYYWXOWGdka&audience=YOUR_API_IDENTIFIER")
				  .asString();
	  
	         mv.addObject("token", response.getBody().toString());
	         System.out.println(response.getBody().toString());
	         jsonobject = (JSONObject) parser.parse(response.getBody().toString());
	         access = (String) jsonobject.get("access_token");
	         scope = (String) jsonobject.get("scope");
	         token_type = (String) jsonobject.get("token_type");
	         expire = (Integer) jsonobject.get("expires_in");
	         System.out.println(access+" ** "+token_type+" ** "+expire);
	         org.json.JSONObject tudo =null;
	         String sigla = null;
	     try {
	           HttpResponse<String> responseuser = Unirest.get("https://data.api.ufg.br/pessoas/comuns/1.0.0/vinculo_ufg_dlog?cpf="+cpf)
			  .header("content-type", "application/json")
			  .header("authorization", token_type+" "+access)
			  .asString();
	       tudo = new org.json.JSONObject(responseuser.getBody().toString());
	      jsonobject = (JSONObject) parser.parse(responseuser.getBody().toString());
	      System.out.println(responseuser.getBody().toString());
	      System.out.println("com o tudo "+tudo);
		  }catch (Exception e) {mv.addObject("msg", "Não foi encontrado usuário para este cpf, erro: "+e);
		     mv.addObject("nome", "");
		     mv.addObject("cpf","");
		     tudo = null;
			// TODO: handle exception
		}
	       if (tudo!=null) {
           nome=(String) jsonobject.get("nome");
           cpf=(String) jsonobject.get("cpf");
         
	       JSONArray array = tudo.getJSONArray("vinculos");
	
	       for(int i=0;i<array.length();i++) {
	     	 org.json.JSONObject f =array.getJSONObject(i);
             user.setNome(nome);	
             user.setCpf(cpf);
             if (f.getString("vinculo").isEmpty()&&f.getString("vinculo").contains("")) {
            	 System.out.println("nome: "+nome+" vinculo: "+f.getString("vinculo")+" array "+i);
		     vinculo.add(f.getString("vinculo"));
		     
             }
		   //  user.setVinculo(vinculo);
		     situacao=f.getString("situacao");
		     System.out.println("nome: "+nome+" situacao: "+f.getString("situacao"));
		     user.setSituacao(situacao);
		     if (f.getString("categoria").isEmpty()) {
		     categoria.add(f.getString("categoria"));
		     }
		   //  user.setCategoria(categoria);
		     if(f.getString("matricula").isEmpty()) {
		     matricula.add(f.getInt("matricula"));		     
		     
		     }
		     user.setMatricula(f.getString("matricula"));
		     ano_ingersso=f.getInt("ano_ingresso");
		     user.setAno_ingresso(ano_ingersso);
		     System.out.println("nome: "+nome+" ano_ingresso: "+f.getString("ano_ingresso"));
		     if(f.getString("unidade_vinculo").isEmpty()) {
		     unidade_vinculo.add(f.getString("unidade_vinculo"));
		    // user.setUnidade_vinculo(unidade_vinculo);
		     }
		     user.setUnidade_vinculo(f.getString("unidade_vinculo"));
		     System.out.println("nome: "+nome+" unidade_vinculo: "+f.getString("unidade_vinculo"));

		     System.out.println("nome: "+nome+" id_unidade_vinculo: "+f.getInt("id_unidade_vinculo"));
		     if (f.getString("sigla_unidade_vinculo").isEmpty()) {
		     sigla_unidade.add(f.getString("sigla_unidade_vinculo"));
		     }
		     sigla = f.getString("sigla_unidade_vinculo");
		     System.out.println("nome: "+nome+" sigla_unidade_vinculo: "+f.getString("sigla_unidade_vinculo"));
		     listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(f.getString("sigla_unidade_vinculo"));
		     user.setSigla_unidade(f.getString("sigla_unidade_vinculo"));
		     cidade=f.getString("cidade_unidade_vinculo");
		     System.out.println("nome: "+nome+" cidade_unidade_vinculo: "+f.getString("cidade_unidade_vinculo"));
		     user.setCidade_vinculo(cidade);	  

		     usuarioExternoRepository.save(user); 
	       }
		     mv.addObject("nome", nome);
		     mv.addObject("cpf",cpf);
		     mv.addObject("msg","");
		     mv.addObject("siglaref", listaorgao);
		     mv.addObject("sigla", sigla);
		     mv.addObject("especie",especieRepository.findAll());
		     mv.addObject("requisicao",requisicaoRepository.findAll());
	     }

	
	
	  Unirest.shutDown();
	
	 
		   
	  }
		  return mv;
	  }
	  @PostMapping("**/salvarsolicitacao")
	  public ModelAndView Salvar(@RequestParam("especie")Integer especie,@RequestParam("ini")Date ini,@RequestParam("fim")Date fim,
			  @RequestParam("destino") String destino,@RequestParam("motivo")String motivo,@RequestParam("requisicao")Long requisicao,
			  @RequestParam("observacao")String observacao,@RequestParam("contato")String contato) {
		  ModelAndView mv = new ModelAndView("acesso.html");
		  
		  return mv;
		  
		  
	  }

	  
	 

}
