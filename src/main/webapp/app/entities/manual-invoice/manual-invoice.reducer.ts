import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IManualInvoice, defaultValue } from 'app/shared/model/manual-invoice.model';

import { IManualInvoiceItem } from 'app/shared/model/manual-invoice-item.model';

export const ACTION_TYPES = {
  SEARCH_MANUALINVOICES: 'manualInvoice/SEARCH_MANUALINVOICES',
  FETCH_MANUALINVOICE_LIST: 'manualInvoice/FETCH_MANUALINVOICE_LIST',
  FETCH_MANUALINVOICE: 'manualInvoice/FETCH_MANUALINVOICE',
  CREATE_MANUALINVOICE: 'manualInvoice/CREATE_MANUALINVOICE',
  UPDATE_MANUALINVOICE: 'manualInvoice/UPDATE_MANUALINVOICE',
  DELETE_MANUALINVOICE: 'manualInvoice/DELETE_MANUALINVOICE',
  RESET: 'manualInvoice/RESET',
  CLEAR_INVOICE_ITEMS: 'manualInvoice/CLEAR_INVOICE_ITEMS',
  ADD_INVOICE_ITEM: 'manualInvoice/ADD_INVOICE_ITEM',
  PAYMENT_DATE_CHANGED: 'manualInvoice/PAYMENT_DATE_CHANGED',
  ITEM_QTY_CHANGED: 'manualInvoice/ITEM_QTY_CHANGED',
  ITEM_PRICE_CHANGED: 'manualInvoice/ITEM_PRICE_CHANGED',
  VAT_RATE_CHANGED: 'manualInvoice/VAT_RATE_CHANGED'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IManualInvoice>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
  subTotal: 0,
  vatAmount: 0,
  total: 0
};

export type ManualInvoiceState = Readonly<typeof initialState>;

// Reducer

export default (state: ManualInvoiceState = initialState, action): ManualInvoiceState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_MANUALINVOICES):
    case REQUEST(ACTION_TYPES.FETCH_MANUALINVOICE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MANUALINVOICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MANUALINVOICE):
    case REQUEST(ACTION_TYPES.UPDATE_MANUALINVOICE):
    case REQUEST(ACTION_TYPES.DELETE_MANUALINVOICE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_MANUALINVOICES):
    case FAILURE(ACTION_TYPES.FETCH_MANUALINVOICE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MANUALINVOICE):
    case FAILURE(ACTION_TYPES.CREATE_MANUALINVOICE):
    case FAILURE(ACTION_TYPES.UPDATE_MANUALINVOICE):
    case FAILURE(ACTION_TYPES.DELETE_MANUALINVOICE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_MANUALINVOICES):
    case SUCCESS(ACTION_TYPES.FETCH_MANUALINVOICE_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MANUALINVOICE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
        subTotal: +action.payload.data.subTotal,
        vatAmount: +action.payload.data.vatAmount,
        total: +action.payload.data.total
      };
    case SUCCESS(ACTION_TYPES.CREATE_MANUALINVOICE):
    case SUCCESS(ACTION_TYPES.UPDATE_MANUALINVOICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MANUALINVOICE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    case ACTION_TYPES.CLEAR_INVOICE_ITEMS:
      const newItemsClearInvoice = [...state.entity.items];
      newItemsClearInvoice.splice(action.payload.itemToRemove, 1);
      return {
        ...state,
        entity: {
          ...state.entity,
          items: newItemsClearInvoice
        },
        ...calculateTotals(state.entity.items, state.entity.vatRate)
      };
    case ACTION_TYPES.ADD_INVOICE_ITEM:
      return {
        ...state,
        entity: {
          ...state.entity,
          items: [...state.entity.items, {}] // todo productId to config
        }
      };
    case ACTION_TYPES.PAYMENT_DATE_CHANGED:
      if (!action.payload.newPaymentDate) {
        return {
          ...state,
          entity: {
            ...state.entity,
            paymentDate: action.payload.newPaymentDate,
            paymentStatus: '',
            paymentType: '',
            paymentAmount: null
          }
        };
      } else if (!state.entity.paymentDate) {
        return {
          ...state,
          entity: {
            ...state.entity,
            paymentDate: action.payload.newPaymentDate,
            paymentStatus: 'Payment Received in Full',
            paymentAmount: +state.total.toFixed(2)
          }
        };
      }
      return {
        ...state
      };
    case ACTION_TYPES.ITEM_QTY_CHANGED:
      const newItemsQtyChanged = updateObjectInArray(state.entity.items, {
        index: action.payload.itemIdx,
        item: {
          ...state.entity.items[action.payload.itemIdx],
          quantity: action.payload.newQty
        }
      });

      return {
        ...state,
        entity: {
          ...state.entity,
          items: newItemsQtyChanged
        },
        ...calculateTotals(newItemsQtyChanged, state.entity.vatRate)
      };
    case ACTION_TYPES.ITEM_PRICE_CHANGED:
      const newItemsPriceChanged = updateObjectInArray(state.entity.items, {
        index: action.payload.itemIdx,
        item: {
          ...state.entity.items[action.payload.itemIdx],
          price: action.payload.newPrice
        }
      });
      return {
        ...state,
        entity: {
          ...state.entity,
          items: newItemsPriceChanged
        },
        ...calculateTotals(newItemsPriceChanged, state.entity.vatRate)
      };
    case ACTION_TYPES.VAT_RATE_CHANGED:
      return {
        ...state,
        ...calculateTotals(state.entity.items, action.payload.newVatRate),
        entity: {
          ...state.entity,
          vatRate: action.payload.newVatRate
        }
      };
    default:
      return state;
  }
};

const apiUrl = 'api/manual-invoices';
const apiSearchUrl = 'api/_search/manual-invoices';

// Actions

export const getSearchEntities: ICrudSearchAction<IManualInvoice> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_MANUALINVOICES,
  payload: axios.get<IManualInvoice>(`${apiSearchUrl}?query=${query}${sort ? `&page=${page}&size=${size}&sort=${sort}` : ''}`)
});

export const getEntities: ICrudGetAllAction<IManualInvoice> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_MANUALINVOICE_LIST,
    payload: axios.get<IManualInvoice>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IManualInvoice> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MANUALINVOICE,
    payload: axios.get<IManualInvoice>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IManualInvoice> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MANUALINVOICE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IManualInvoice> = entity => async dispatch => {
  // remove any empty fields rather than pass them as empty strings
  Object.keys(entity).forEach(key => entity[key] === '' && delete entity[key]);
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MANUALINVOICE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IManualInvoice> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MANUALINVOICE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});

export const clearInvoiceItems = i => ({
  type: ACTION_TYPES.CLEAR_INVOICE_ITEMS,
  payload: { itemToRemove: i }
});

export const addInvoiceItem = () => ({
  type: ACTION_TYPES.ADD_INVOICE_ITEM
});

export const paymentDateChanged = newPaymentDate => ({
  type: ACTION_TYPES.PAYMENT_DATE_CHANGED,
  payload: { newPaymentDate }
});

export const itemQtyChanged = (itemIdx, newQty) => ({
  type: ACTION_TYPES.ITEM_QTY_CHANGED,
  payload: {
    itemIdx,
    newQty
  }
});

export const itemPriceChanged = (itemIdx, newPrice) => ({
  type: ACTION_TYPES.ITEM_PRICE_CHANGED,
  payload: {
    itemIdx,
    newPrice
  }
});

export const vatRateChanged = newVatRate => ({
  type: ACTION_TYPES.VAT_RATE_CHANGED,
  payload: {
    newVatRate
  }
});

const calculateTotals = (items, vatRate) => {
  const subTotal = +items.reduce((a, b) => a + (b.price || 0) * (b.quantity || 0), 0).toFixed(2);
  const vatAmount = +(subTotal * (vatRate / 100)).toFixed(2);
  const total = +(subTotal + +vatAmount).toFixed(2);
  return { subTotal, vatAmount, total };
};

const updateObjectInArray = (array, action): IManualInvoiceItem[] =>
  array.map((item, index) => {
    if (index !== action.index) {
      // This isn't the item we care about - keep it as-is
      return item;
    }

    // Otherwise, this is the one we want - return an updated value
    return {
      ...item,
      ...action.item
    };
  });
