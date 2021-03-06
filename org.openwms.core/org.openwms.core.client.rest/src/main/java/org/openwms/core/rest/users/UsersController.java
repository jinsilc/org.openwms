/*
 * openwms.org, the Open Warehouse Management System.
 * Copyright (C) 2014 Heiko Scherrer
 *
 * This file is part of openwms.org.
 *
 * openwms.org is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as 
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 *
 * openwms.org is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software. If not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.openwms.core.rest.users;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.openwms.core.domain.system.usermanagement.User;
import org.openwms.core.domain.system.usermanagement.UserPassword;
import org.openwms.core.rest.AbstractWebController;
import org.openwms.core.rest.BeanMapper;
import org.openwms.core.rest.ExceptionCodes;
import org.openwms.core.rest.HttpBusinessException;
import org.openwms.core.rest.ResponseVO;
import org.openwms.core.service.UserService;
import org.openwms.core.service.exception.EntityNotFoundException;
import org.openwms.core.service.exception.ServiceRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * An UsersController represents a RESTful access to <tt>User</tt>s. It is transactional by the means it is the outer application service
 * facade that returns validated and completed <tt>User</tt> objects to its clients.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: $
 * @since 0.1
 */
@Controller
@RequestMapping("/users")
public class UsersController extends AbstractWebController {

    @Autowired
    private UserService service;
    @Autowired
    private BeanMapper<User, UserVO> mapper;

    /**
     * This method returns all existing <tt>User</tt>s.
     * 
     * <p>
     * <table>
     * <tr>
     * <td>URI</td>
     * <td>/users</td>
     * </tr>
     * <tr>
     * <td>Verb</td>
     * <td>GET</td>
     * </tr>
     * <tr>
     * <td>Auth</td>
     * <td>YES</td>
     * </tr>
     * <tr>
     * <td>Header</td>
     * <td></td>
     * </tr>
     * </table>
     * </p>
     * <p>
     * The response stores <tt>User</tt> instances JSON encoded. It contains a collection of <tt>User</tt> objects.
     * </p>
     * 
     * @return JSON response
     */
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseVO> findAllUsers() {
        Collection<UserVO> users = mapper.map(service.findAll(), UserVO.class);
        ResponseVO response = new ResponseVO();
        response.add(new ResponseVO.ItemBuilder().wStatus(HttpStatus.OK).wParams(users.toArray()).build());
        return new ResponseEntity<ResponseVO>(response, HttpStatus.OK);
    }

    /**
     * Takes a newly created <tt>User</tt> instance and persists it.
     * 
     * <p>
     * <table>
     * <tr>
     * <td>URI</td>
     * <td>/users</td>
     * </tr>
     * <tr>
     * <td>Verb</td>
     * <td>POST</td>
     * </tr>
     * <tr>
     * <td>Auth</td>
     * <td>YES</td>
     * </tr>
     * <tr>
     * <td>Header</td>
     * <td></td>
     * </tr>
     * </table>
     * </p>
     * <p>
     * Request Body
     * 
     * <pre>
     *   {
     *     "username" : "testuser"
     *   }
     * </pre>
     * 
     * Parameters:
     * <ul>
     * <li>username (String):</li>
     * The unique username.
     * </ul>
     * </p>
     * <p>
     * Response Body
     * 
     * <pre>
     *   {
     *     "id" : 4711,
     *     "username" : "testuser",
     *     "token" : "876sjh36ejwhd",
     *     "version" : 1
     *   }
     * </pre>
     * 
     * <ul>
     * <li>id (Integer (32bit)):</li>
     * The internal unique technical key for the stored instance.
     * <li>username (String):</li>
     * The unique username.
     * <li>token (String):</li>
     * A generated token that is used to authenticate each request.
     * <li>version (Integer (32bit)):</li>
     * A version number used internally for optimistic locking.
     * </ul>
     * </p>
     *
     * @param user The user to create
     * @return a responseVO
     */
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Content-Type=application/json")
    @ResponseBody
    public ResponseEntity<ResponseVO> create(@RequestBody @Valid @NotNull UserVO user) {

        ResponseVO result = new ResponseVO();
        HttpStatus resultStatus = HttpStatus.CREATED;
        try {
            UserVO res = mapper.map(service.create(mapper.mapBackwards(user, User.class)), UserVO.class);
            result.add(new ResponseVO.ItemBuilder().wStatus(HttpStatus.CREATED).wParams(res).build());
        } catch (ServiceRuntimeException sre) {
            resultStatus = HttpStatus.NOT_ACCEPTABLE;
            ResponseVO.ResponseItem item = new ResponseVO.ItemBuilder().wMessage(sre.getMessage())
                    .wStatus(resultStatus).wParams(user.getUsername()).build();
            result.add(item);
        }
        return new ResponseEntity<ResponseVO>(result, resultStatus);
    }

    @RequestMapping(value = "/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, headers = "Content-Type=application/json")
    @ResponseBody
    public User getUserById(@RequestParam("username") @NotNull @Size(min = 1) String pUsername) {
        // TODO [scherrer] : clarify if this is necessary
        User user = findByUsername(pUsername);
        if (user == null) {
            throw new IllegalArgumentException("User with usernayme " + pUsername + " not found");
        }
        return user;
    }

    /**
     * FIXME [scherrer] Comment this
     * 
     * @param user
     * @return a responseVO
     */
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Transactional
    public ResponseEntity<ResponseVO> save(@RequestBody @Valid UserVO user) {
        if (user.getId() == null) {
            String msg = translate(ExceptionCodes.USER_IS_TRANSIENT, user.getUsername());
            throw new HttpBusinessException(msg, HttpStatus.NOT_ACCEPTABLE);
        }

        User persistedUser = service.findById(user.getId());
        if (persistedUser.getVersion() != user.getVersion()) {
            throw new HttpBusinessException(translate(ExceptionCodes.USER_HAS_CHANGED), HttpStatus.NOT_ACCEPTABLE);
        }

        User toSave = mapper.mapBackwards(user, User.class);
        persistedUser = mapper.mapFromTo(toSave, persistedUser);
        UserVO saved = mapper.map(service.save(persistedUser), UserVO.class);
        ResponseVO result = new ResponseVO(new ResponseVO.ItemBuilder().wParams(saved).wStatus(HttpStatus.OK).build());
        return new ResponseEntity<ResponseVO>(result, HttpStatus.CREATED);
    }

    /**
     * FIXME [scherrer] Comment this
     *
     * @param image The image to save
     * @param id The users persisted id
     *
     * @return An responseVO
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<ResponseVO> saveImage(@RequestBody @NotNull byte[] image, @PathVariable("id") @NotNull Long id) {
        if (id == null) {
            String msg = translate(ExceptionCodes.USER_IS_TRANSIENT);
            throw new HttpBusinessException(msg, HttpStatus.NOT_FOUND);
        }
        User persistedUser = service.findById(id);
        service.uploadImageFile(persistedUser.getUsername(), image);
        UserVO saved = mapper.map(service.findById(id), UserVO.class);
        ResponseVO result = new ResponseVO(new ResponseVO.ItemBuilder().wParams(saved).wStatus(HttpStatus.OK).build());
        return new ResponseEntity<ResponseVO>(result, HttpStatus.CREATED);
    }

    /**
     * FIXME [scherrer] Comment this
     *
     * @param names
     * @return a responseVO
     * @throws Exception
     */
    @RequestMapping(value = "/{name}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseVO> remove(@PathVariable("name") @NotNull String... names) {
        ResponseVO result = new ResponseVO();
        HttpStatus resultStatus = HttpStatus.OK;
        for (String name : names) {
            if (name == null || name.isEmpty()) {
                continue;
            }
            try {
                service.removeByBK(new String[] { (name) });
                result.add(new ResponseVO.ItemBuilder().wStatus(HttpStatus.OK).wParams(name).build());
            } catch (ServiceRuntimeException sre) {
                resultStatus = HttpStatus.NOT_FOUND;
                ResponseVO.ResponseItem item = new ResponseVO.ItemBuilder().wMessage(sre.getMessage())
                        .wStatus(HttpStatus.INTERNAL_SERVER_ERROR).wParams(name).build();
                if (EntityNotFoundException.class.equals(sre.getClass())) {
                    item.httpStatus = HttpStatus.NOT_FOUND;
                }
                result.add(item);
            }
        }
        return new ResponseEntity<ResponseVO>(result, resultStatus);
    }

    private User findByUsername(String pUsername) {
        List<User> users = service.findAll();
        for (User user : users) {
            if (user.getUsername().equals(pUsername)) {
                return user;
            }
        }
        return null;
    }

    // @RequestMapping(method = RequestMethod.PUT, produces =
    // MediaType.APPLICATION_JSON_VALUE, consumes =
    // MediaType.APPLICATION_JSON_VALUE, headers =
    // "Content-Type=application/json")
    // @ResponseBody
    public void changeUserPassword(@RequestBody UserPassword userPassword) {
        service.changeUserPassword(userPassword);
    }

}
