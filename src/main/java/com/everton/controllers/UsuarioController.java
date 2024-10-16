package com.everton.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.everton.dtos.UsuarioDto;
import com.everton.models.UsuarioModel;
import com.everton.repositories.UsuarioRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
	@Autowired
	UsuarioRepository repository;

	@GetMapping("/")
	public String index() {
		return "usuario/index";
	}

	@GetMapping("/inserir/")
	public String inserir() {
		return "usuario/inserir";
	}

	@PostMapping("/inserir/")
	public String inserirBD(
			@ModelAttribute @Valid UsuarioDto usuarioDTO,
			BindingResult result, RedirectAttributes msg,
			@RequestParam("file") MultipartFile imagen) {
		if (result.hasErrors()) {
			msg.addFlashAttribute("erroCadastrar", "Erro ao cadastrar novo usuário");
			return "redirect:/usuario/inserir/";
		}
		var usuarioModel = new UsuarioModel();
		BeanUtils.copyProperties(usuarioDTO, usuarioModel);
		usuarioModel.setTipo("comum");
		try {
			if (!imagen.isEmpty()) {
				byte[] bytes = imagen.getBytes();
				System.out.println(imagen.getOriginalFilename());
				Path camino = Paths
						.get("/Users/gonzar/Downloads/Spring20242-main/everton/everton/src/main/resources/static/img/"
								+ imagen.getOriginalFilename());
				Files.write(camino, bytes);
				usuarioModel.setImagen(imagen.getOriginalFilename());
			}
		} catch (IOException e) {
			System.out.println("erro imagen");
		}
		repository.save(usuarioModel);
		msg.addFlashAttribute("sucessoCadastrar", "Usuario cadastrado!");
		return "redirect:../listar/";
	}

	@PostMapping("/listar/")
	public ModelAndView listarComFiltro(@RequestParam("busca") String busca) {
		ModelAndView mv = new ModelAndView("usuario/listar");
		List<UsuarioModel> lista = repository.findUsuarioByNomeLike("%" + busca + "%");
		mv.addObject("usuarios", lista);
		return mv;
	}

	@GetMapping("/listar/{tipo}")
	public ModelAndView listarPorTipo(@PathVariable("tipo") String busca) {
		ModelAndView mv = new ModelAndView("usuario/listar");
		List<UsuarioModel> lista = repository.findUsuarioByTipo(busca);
		mv.addObject("usuarios", lista);
		return mv;
	}

	@GetMapping("/listar/")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("usuario/listar");
		List<UsuarioModel> lista = repository.findAll();
		mv.addObject("usuarios", lista);
		return mv;
	}

	@SuppressWarnings("null") // agrego esto
	@GetMapping("/mostrarImagen/{img}")
	@ResponseBody
	public byte[] mostrarImagen(@PathVariable(value = "img") String img) throws IOException {
		File imagenArchivo = new File(
				"/Users/gonzar/Downloads/Spring20242-main/everton/everton/src/main/resources/static/img/" + img);// modifique
																													// aca
		if (img != null || img.trim().length() > 0) {
			return Files.readAllBytes(imagenArchivo.toPath());
		}
		return null;
	}

	@GetMapping("/editar/{Id}")
	public ModelAndView editar(@PathVariable(value = "Id") int Id) {
		ModelAndView mv = new ModelAndView("usuario/editar");
		Optional<UsuarioModel> usuario = repository.findById(Id);
		mv.addObject("Id", usuario.get().getId());
		mv.addObject("Email", usuario.get().getEmail());
		mv.addObject("Nome", usuario.get().getNome());
		mv.addObject("Senha", usuario.get().getSenha());
		mv.addObject("Tipo", usuario.get().getTipo());
		return mv;
	}

	@PostMapping("/editar/{Id}")
	public String editarBD(
			@ModelAttribute @Valid UsuarioDto usuarioDTO,
			BindingResult result, RedirectAttributes msg, @PathVariable(value = "Id") int Id) {
		Optional<UsuarioModel> usuario = repository.findById(Id);
		if (result.hasErrors()) {
			msg.addFlashAttribute("erroEditar", "Erro ao editar usuário");
			return "redirect:/usuario/listar/";
		}
		var usuarioModel = usuario.get();
		BeanUtils.copyProperties(usuarioDTO, usuarioModel);
		repository.save(usuarioModel);
		msg.addFlashAttribute("sucessoEditar", "Usuario editado!");
		return "redirect:../../usuario/listar/";
	}

	@GetMapping("/excluir/{Id}")
	public String excluir(@PathVariable(value = "Id") int Id) {
		Optional<UsuarioModel> usuario = repository.findById(Id);
		if (usuario.isEmpty()) {
			return "redirect:../../usuario/listar/";
		}
		repository.deleteById(Id);
		return "redirect:../../usuario/listar/";
	}
}
