package ufg.br.solicitadlog.api;



import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.hibernate.query.criteria.internal.expression.function.TrimFunction;
import org.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import ufg.br.solicitadlog.classe.Orgao;
import ufg.br.solicitadlog.classe.SolicitacaoFant;
import ufg.br.solicitadlog.classe.Usuario_Externo;
import ufg.br.solicitadlog.classe.solicitacao;
import ufg.br.solicitadlog.repository.*;


@Controller
public class Api {
	//linha abaixo com variaveis para receber os dados da api
	  String nome; String cpf1;List<String> vinculo = null;List<String> categoria = null;List<Integer> matricula = null;String situacao;Integer ano_ingersso;
	  List<String> unidade_vinculo = null;List<Integer> id_unidade = null;List<String> sigla_unidade = null;String cidade; List<String> sigla_Referencia = null;
	  String unidade;
	  String CPF;
	  String mensagem;
	  Orgao orgao = new Orgao();
	  Usuario_Externo ue = new Usuario_Externo();
	  Long[] cd_orgao;
	  ArrayList<Orgao> listaorgao = new ArrayList<>();
	  Usuario_Externo user = new Usuario_Externo();
	@Autowired
	private UsuarioExternoRepository usuarioExternoRepository;
	@Autowired
	private OrgaoRepository orgaoRepository;
	@Autowired
	private EspecieRepository especieRepository;
	@Autowired
	private RequisicaoRepository requisicaoRepository;
	@Autowired
	private SolicitacaoRepository solicitacaoRepository;
	@Autowired
	private SolicitacaoFantRepository solicitacaoFantRepository;
	
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
	     mv.addObject("lista", new SolicitacaoFant());
		 return mv;
	 }
	
	  @GetMapping("**/acesso")	
	  public ModelAndView Acesso(@RequestParam("cpf") String cpf) throws ParseException   {
		  //Linha abaixo com variaveis para receber o token de acesso
		  String access; String scope; String token_type; Integer expire;
		  CPF=cpf;
		  ModelAndView mv = new ModelAndView("acesso.html");
            	 CPFValidator cpfvalidator = new CPFValidator();
            	 List<ValidationMessage> erro = cpfvalidator.invalidMessagesFor(cpf);
            	 JSONObject json = new JSONObject();
            	 if (erro.size()>0) {
            		 mensagem="CPF invalido";
            	 }else {
            		 ue=(Usuario_Externo) usuarioExternoRepository.verExistenciaLocalDeUsuarioExterno(cpf);
            		 if (Objects.isNull(ue)) {
            			 org.json.JSONObject js = new org.json.JSONObject();
            			 JSONParser ps = new JSONParser();
       		                HttpResponse<String> response = Unirest.post("https://data.api.ufg.br/token")
       						  .header("content-type", "application/x-www-form-urlencoded")
       						  .body("grant_type=client_credentials&client_id=IJzJzes8Y06GzZ52b8sT10F3uNIa&client_secret=R9jXFT1NQXZz799BYFYYWXOWGdka&audience=YOUR_API_IDENTIFIER")
       						  .asString();            			 
       		             mv.addObject("token", response.getBody().toString());
       		             System.out.println(response.getBody().toString()); 
       		             json =  (JSONObject) ps.parse(response.getBody().toString());
       		             
       		          access = (String) json.get("access_token");
       		          scope = (String) json.get("scope");
       		          token_type = (String) json.get("token_type");
       		          expire = (Integer) json.get("expires_in");
       		        //  System.out.println(access+" ** "+token_type+" ** "+expire);
       		          
       		          org.json.JSONObject tudo =null;
       		          String sigla = null;
     	             try {
   	                  HttpResponse<String> responseuser = Unirest.get("https://data.api.ufg.br/pessoas/comuns/1.0.0/vinculo_ufg_dlog?cpf="+cpf)
   			           .header("content-type", "application/json")
   			           .header("authorization", token_type+" "+access)
   			           .asString();
   	               System.out.println("Chegou aqui "+cpf+" *** "+access);
   	                   tudo = new org.json.JSONObject(responseuser.getBody().toString());
   	                   json = (JSONObject) ps.parse(responseuser.getBody().toString());
     	               JSONArray array = tudo.getJSONArray("vinculos");
     	               nome=(String) json.get("nome");
                       CPF=(String) json.get("cpf");
                        if (array.length()>0) {
                        	for(int i=0;i<array.length();i++) {
                        		org.json.JSONObject f =array.getJSONObject(i);
                        		user.setNome(nome);	
                                user.setCpf(cpf);
                                user.setVinculo(f.getString("vinculo"));
                                user.setSituacao(f.getString("situacao"));
                                user.setCategoria(f.getString("categoria"));
                                user.setMatricula(f.getString("matricula"));
                                user.setAno_ingresso(f.getInt("ano_ingresso"));
                                user.setUnidade_vinculo(f.getString("unidade_vinculo"));
                                listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(f.getString("sigla_unidade_vinculo"));
                                cd_orgao=orgaoRepository.buscaCodigoPorSg_orgao(f.getString("sigla_unidade_vinculo"));
                                for(Long c:cd_orgao) {
         		            	   user.setId_unidade(c);
         		               }
                                user.setSigla_unidade(f.getString("sigla_unidade_vinculo"));
                                user.setCidade_vinculo(f.getString("cidade_unidade_vinculo"));
                                String ativo=f.getString("situacao");ativo.trim();ativo.replace("","");
                                if(ativo.contains("Ativo")) {
             		               usuarioExternoRepository.save(user);
             		               mv.addObject("verificador","externo");
             		               mv.addObject("visivel",1);
     			                   mv.addObject("nome", json.get("nome"));
     			                   mv.addObject("cpf",json.get("cpf"));
     			                   mv.addObject("siglaref", listaorgao);
     			                   mv.addObject("sigla", f.getString("sigla_unidade_vinculo"));
             		               mensagem="Insira a senha";
             		              
                                     }else {
                                    	   mensagem="Usuário não autorizado "+" ["+ativo+"]";             		                       
             			                   mv.addObject("nome", "");
             			                   mv.addObject("cpf","");
             			                   listaorgao=new ArrayList<>();
             			                   mv.addObject("siglaref", listaorgao);
             			                   mv.addObject("sigla", sigla);
             		               }
                               mv.addObject("especie",especieRepository.findAll());
           		               mv.addObject("requisicao",requisicaoRepository.findAll());
           		               mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
           		               listaorgao=new ArrayList<>();
                        	}
                        	
                        }else {
   			                   mv.addObject("nome", "");
   			                   mv.addObject("cpf","");
   			                   mensagem="Não foi encontrado usuário para este cpf";   			                  
   			                   listaorgao=new ArrayList<>();
   			                   mv.addObject("siglaref", listaorgao);
   			                   mv.addObject("sigla", "");
   			                   mv.addObject("especie",especieRepository.findAll());
   			                   mv.addObject("requisicao",requisicaoRepository.findAll());
   			                   mv.addObject("lista", new SolicitacaoFant());
   			                   mv.addObject("verificador", "");
   			                   mv.addObject("visivel",0);
                        }
   	  
   		             }catch (Exception e) {mensagem="Nada foi encontrado para este cpf";
   		              mv.addObject("nome", "");
   		              mv.addObject("cpf","");
   		              tudo = null;
   			
   		             }
     	            Unirest.shutDown();
            		 }else {
            			 nome=ue.getNome();
            			 CPF=ue.getCpf();
    				     mv.addObject("verificador", "interno");
    				     mv.addObject("nome", ue.getNome());
    				     mv.addObject("cpf",ue.getCpf());
    				     mv.addObject("sigla", ue.getSigla_unidade());
    				     mv.addObject("visivel", 1);
                         mv.addObject("usuario", ue)  ;
    		             mv.addObject("especie",especieRepository.findAll());
    		             mv.addObject("requisicao",requisicaoRepository.findAll());
    		             mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
    		             listaorgao=new ArrayList<>();
    		             mensagem="Digite sua senha";
            		 }
            		 
            	 }
            System.out.println(mensagem);
            mv.addObject("msg",mensagem);

		  return mv;
	  }
	  @PostMapping("**/salvarsolicitacao")
	  public ModelAndView Salvar(@RequestParam("especie")Integer especie,@RequestParam("ini")String ini,@RequestParam("fim")String fim,
			  @RequestParam("destino") String destino,@RequestParam("motivo")String motivo,@RequestParam("requisicao")Long requisicao,
			  @RequestParam("contato")String contato) throws java.text.ParseException {
		  ModelAndView mv = new ModelAndView("acesso.html");
		  Long id = solicitacaoRepository.verMaximoId();
		  
		  System.out.println("Testando o post: "+nome+" cpf "+CPF);
		  if (id==null) {
			  id=(long) 1;
		  }else {
			  id=id+1;
		  }
		  if (especie!=null) {
			  if (ini!=null) {
				  if (fim!=null){
					  if (!destino.isEmpty()&&!destino.isBlank()) {
						  if (!motivo.isEmpty()&&!motivo.isBlank()) {
							  if(requisicao!=null) {
								  if(!contato.isEmpty()&&!contato.isBlank()) {
									  solicitacao sol = new solicitacao();
									  sol.setEspecie(especie);
									  sol.setCpf_solicitante(CPF);
									  SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
									  sol.setData_ini(formatter.parse(ini));
									  sol.setData_fim(formatter.parse(fim));
									  sol.setDestino(destino);
									  sol.setMotivo(motivo);
									  sol.setId(id);
									  sol.setNome_unidade(unidade);
									  sol.setRequisicao(requisicao);
									  if (nome.equals(null)) {
										  sol.setSolicitante(ue.getNome());
									  }else {
									  sol.setSolicitante(nome);
									  }
								      sol.setContato(contato);
									  sol.setData_solicitacao(new Date());
									/*  for(Long c:cd_orgao) {
									  sol.setUnidade(c);
									  }*/
									  sol.setUnidade(ue.getId_unidade());
									  solicitacaoRepository.save(sol);
									  mv.addObject("msg", "Solicitação realizada com sucesso com ID= "+id);
									  mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
								  }else {
									  mv.addObject("msg", "Deve ser inserido o contato da solciitação");
								  }
							  }else {
								  mv.addObject("msg", "deve ser indicada se a requisição e urbana ou para viagem");
							  }
						  }else {
							  mv.addObject("msg", "Deve ser preenchido o motivo da solciitação");
						  }
					  }else {
						  mv.addObject("msg", "Deve ser definido o destino da solciitação");
					  }
				  }else {
					  mv.addObject("msg", "Deve ser escolhido a data final da solciitação"); 
				  }
			  }else {
				  mv.addObject("msg", "Deve ser escolhido a data inicial da solciitação");
			  }
		  }else {
			  mv.addObject("msg", "Deve ser escolhido o tipo de veículo");
		  }
		  
		  return mv;
		  
		  
	  }
	  @PostMapping("**/verificainseresenha")
	  public ModelAndView VerificaInsereSenha(@RequestParam("digitesenha")String digitesenha,@RequestParam("inseresenha")String inseresenha,
			  @RequestParam("repitasenha") String repitasenha) throws java.text.ParseException {
		  ModelAndView mv = new ModelAndView("acesso.html");
		  
		  if(digitesenha.isEmpty()&&digitesenha.isBlank()) {
			  if(!inseresenha.isBlank()&&!inseresenha.isEmpty()) {
				  
				  if(!repitasenha.isBlank()&&!repitasenha.isEmpty()) {
					  System.out.println("insere= "+inseresenha+" repita= "+repitasenha);
					  if(inseresenha.equals(repitasenha)) {
						  
						  user.setSenha(inseresenha); 
						  usuarioExternoRepository.save(user);
				           mv.addObject("nome", nome);
				           mv.addObject("cpf",CPF);
				           mv.addObject("siglaref", listaorgao);
				           mv.addObject("sigla", null);
				           mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
				           mv.addObject("msg","Preencha a solicitação e clique em Salvar");
				           mv.addObject("especie",especieRepository.findAll());
				           mv.addObject("requisicao",requisicaoRepository.findAll());
				            mv.addObject("verificador", "");
				            mv.addObject("visivel",0);
					  }else {mv.addObject("msg", "As senhas não conferem, digite novamente");
		                     mv.addObject("verificador","externo");
		                     mv.addObject("visivel",1);
					  }
					   
				  }else {mv.addObject("msg", "Deve ser preenchido o campo de repetição da senha, que deve ser igual ao da inserção "); 
				  }
			  }else {mv.addObject("msg", "O campo de inserção de senha está vazio");}
		  }else { 
			  if(ue.getSenha().equals(digitesenha)) {
		             mv.addObject("nome", ue.getNome());
		             System.out.println("Nome "+ue.getNome()+" cpf "+ue.getCpf()+" cd= "+ue.getId_unidade());
		         //    cd_orgao=ue.getId_unidade();
		             mv.addObject("cpf", ue.getCpf());
		             mv.addObject("msg","Preencha a solicitação e clique em Salvar");
		             listaorgao =  (ArrayList<Orgao>) orgaoRepository.pesquisaOrgaoPorSigla(ue.getSigla_unidade());
		             mv.addObject("siglaref", listaorgao);
		             mv.addObject("especie",especieRepository.findAll());
		             mv.addObject("requisicao",requisicaoRepository.findAll());
		             mv.addObject("sigla", ue.getSigla_unidade());
		             mv.addObject("verificador", "");
		             mv.addObject("visivel",0);	
		             mv.addObject("lista",solicitacaoFantRepository.solicitacoesPorCpf(CPF));
			  }else {
				     mv.addObject("msg","Senha não confere, começe novamente");
		             mv.addObject("nome", "");
		             mv.addObject("cpf","");		           
		             listaorgao=new ArrayList<>();
		             mv.addObject("siglaref", listaorgao);
		             mv.addObject("sigla", "");
		             mv.addObject("especie",especieRepository.findAll());
		             mv.addObject("requisicao",requisicaoRepository.findAll());
		             mv.addObject("lista", new SolicitacaoFant());
		             mv.addObject("verificador", "");
		             mv.addObject("visivel",0);
			  }
			  
		  }
		  
		  
		  return mv;
	  }

	  
	 

}
