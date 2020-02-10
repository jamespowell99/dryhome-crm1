import axios from 'axios';

import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICustomer, defaultValue } from 'app/shared/model/customer.model';

import { ICustomerOrder } from 'app/shared/model/customer-order.model';

import { ICrudGetOrdersByCustomerIdAction } from 'app/entities/customer.redux-action-type';

export const ACTION_TYPES = {
  SEARCH_CUSTOMERS: 'customer/SEARCH_CUSTOMERS',
  FETCH_CUSTOMER_LIST: 'customer/FETCH_CUSTOMER_LIST',
  FETCH_CUSTOMER: 'customer/FETCH_CUSTOMER',
  CREATE_CUSTOMER: 'customer/CREATE_CUSTOMER',
  UPDATE_CUSTOMER: 'customer/UPDATE_CUSTOMER',
  DELETE_CUSTOMER: 'customer/DELETE_CUSTOMER',
  SET_BLOB: 'customer/SET_BLOB',
  RESET: 'customer/RESET',
  FETCH_CUSTOMER_ORDER_LIST: 'customer/FETCH_CUSTOMER_ORDER_LIST',
  PRINT_DOCUMENT: 'customer/PRINT_DOCUMENT',
  DOWNLOAD_DOCUMENT: 'customer/DOWNLOAD_DOCUMENT'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICustomer>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  customerOrders: [] as ReadonlyArray<ICustomerOrder>,
  totalOrders: 0,
  retrievedCustomer: false,
  retrievedCustomerOrders: false,
  generatingDocument: false,
  printDocumentBlob: null,
  downloadDocumentBlob: null
};

export type CustomerState = Readonly<typeof initialState>;

// Reducer

export default (state: CustomerState = initialState, action): CustomerState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CUSTOMERS):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMER):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMER_ORDER_LIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
        retrievedCustomer: false,
        retrievedCustomerOrders: false
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
    case REQUEST(ACTION_TYPES.PRINT_DOCUMENT):
      return {
        ...state,
        generatingDocument: true,
        printDocumentBlob: null,
        downloadDocumentBlob: null,
        errorMessage: null
      };
    case REQUEST(ACTION_TYPES.DOWNLOAD_DOCUMENT):
      return {
        ...state,
        generatingDocument: true,
        printDocumentBlob: null,
        downloadDocumentBlob: null,
        errorMessage: null
      };
    case FAILURE(ACTION_TYPES.SEARCH_CUSTOMERS):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMER):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMER):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMER):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMER):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMER_ORDER_LIST):
      return {
        ...state,
        retrievedCustomer: false,
        retrievedCustomerOrders: false,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case FAILURE(ACTION_TYPES.PRINT_DOCUMENT):
      return {
        ...state,
        errorMessage: action.payload,
        generatingDocument: false,
        printDocumentBlob: null,
        downloadDocumentBlob: null
      };
    case FAILURE(ACTION_TYPES.DOWNLOAD_DOCUMENT):
      return {
        ...state,
        errorMessage: action.payload,
        generatingDocument: false,
        printDocumentBlob: null,
        downloadDocumentBlob: null
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
        entity: action.payload.data,
        retrievedCustomer: true,
        loading: state.retrievedCustomerOrders
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMER_ORDER_LIST):
      return {
        ...state,
        retrievedCustomerOrders: true,
        loading: state.retrievedCustomer,
        customerOrders: action.payload.data,
        totalOrders: action.payload.headers['x-total-count']
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
    case SUCCESS(ACTION_TYPES.PRINT_DOCUMENT):
      return {
        ...state,
        generatingDocument: false,
        printDocumentBlob: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DOWNLOAD_DOCUMENT):
      return {
        ...state,
        generatingDocument: false,
        downloadDocumentBlob: action.payload.data
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

export const getCustomerOrders: ICrudGetOrdersByCustomerIdAction<ICustomerOrder> = (customerId, page, size) => {
  const requestUrl = `api/customer-orders?customerId.equals=${customerId}&sort=orderDate,desc${page ? `&page=${page}&size=${size}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMER_ORDER_LIST,
    payload: axios.get<ICustomerOrder>(`${requestUrl}&cacheBuster=${new Date().getTime()}`)
  };
};

export const printDocument = (docName, entityId) => ({
  type: ACTION_TYPES.PRINT_DOCUMENT,
  payload: axios.get(`api/customers/${entityId}/document?documentName=${docName}&type=PDF`, { responseType: 'blob' })
});

export const downloadDocument = (docName, entityId) => ({
  type: ACTION_TYPES.DOWNLOAD_DOCUMENT,
  payload: axios.get(`api/customers/${entityId}/document?documentName=${docName}&type=DOCX`, { responseType: 'blob' })
});
