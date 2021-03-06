import axios from 'axios';
import { ICrudGetAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';
import { ICrudSearchCustomerAction, ICrudGetAllCustomerAction } from 'app/entities/customer.redux-action-type';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomer, defaultValue } from 'app/shared/model/customer.model';

import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export const ACTION_TYPES = {
  SEARCH_CUSTOMERS: 'customer/SEARCH_CUSTOMERS',
  FETCH_CUSTOMER_LIST: 'customer/FETCH_CUSTOMER_LIST',
  FETCH_CUSTOMER: 'customer/FETCH_CUSTOMER',
  CREATE_CUSTOMER: 'customer/CREATE_CUSTOMER',
  UPDATE_CUSTOMER: 'customer/UPDATE_CUSTOMER',
  DELETE_CUSTOMER: 'customer/DELETE_CUSTOMER',
  SET_BLOB: 'customer/SET_BLOB',
  RESET: 'customer/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomer>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CustomerState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerState = initialState, action): CustomerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CUSTOMERS):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMER):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMER):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CUSTOMERS):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMER):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMER):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMER):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CUSTOMERS):
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMER_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMER):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.SET_BLOB:
      const { name, data, contentType } = action.payload;
      return {
        ...state,
        entity: {
          ...state.entity,
          [name]: data,
          [name + 'ContentType']: contentType
        }
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/customers';
const apiSearchUrl = 'api/_search/customers';

// Actions

export const getSearchEntities: ICrudSearchCustomerAction<ICustomer> = (
  searchId,
  searchCompanyName,
  searchTown,
  searchPostCode,
  searchLastName,
  searchTel,
  searchMob,
  type,
  page,
  size,
  sort
) => {
  let requestUrl = `${apiUrl}?type.equals=${type}`;
  requestUrl += `${searchId ? `&id.equals=${searchId}` : ''}`;
  requestUrl += `${searchCompanyName ? `&companyName.contains=${searchCompanyName}` : ''}`;
  requestUrl += `${searchTown ? `&town.contains=${searchTown}` : ''}`;
  requestUrl += `${searchPostCode ? `&postCode.contains=${searchPostCode}` : ''}`;
  requestUrl += `${searchLastName ? `&lastName.contains=${searchLastName}` : ''}`;
  requestUrl += `${searchTel ? `&tel.contains=${searchTel}` : ''}`;
  requestUrl += `${searchMob ? `&mobile.contains=${searchMob}` : ''}`;
  requestUrl += `${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.SEARCH_CUSTOMERS,
    payload: axios.get<ICustomer>(requestUrl)
  };
};

export const getEntities: ICrudGetAllCustomerAction<ICustomer> = (type, page, size, sort) => {
  const requestUrl = `${apiUrl}?type.equals=${type}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMER_LIST,
    payload: axios.get<ICustomer>(`${requestUrl}&cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICustomer> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMER,
    payload: axios.get<ICustomer>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICustomer> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);

  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMER,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities('DAMP_PROOFER', 0, ITEMS_PER_PAGE, 'id,desc'));
  return result;
};

export const updateEntity: ICrudPutAction<ICustomer> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMER,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities('DAMP_PROOFER', 0, ITEMS_PER_PAGE, 'id,desc'));
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomer> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMER,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities('DAMP_PROOFER', 0, ITEMS_PER_PAGE, 'id,desc'));
  return result;
};

export const setBlob = (name, data, contentType?) => ({
  type: ACTION_TYPES.SET_BLOB,
  payload: {
    name,
    data,
    contentType
  }
});

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
