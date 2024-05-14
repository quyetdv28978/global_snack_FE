import { defineStore } from 'pinia';
import axios from '@/service/Authentication/http.js';

const apiNCC = 'http://localhost:8080/ncc';

export const useNCCService = defineStore('nha-cung-cap', {
    state: () => ({
        data: []
    }),
    actions: {
        //load tất cả data
        async fetchData() {
            try {
               
                const response = await axios.get(apiNCC).then(data => {
                    console.log(data.data._embedded);
                    return data.data._embedded
                });
                this.data = response;
            } catch (error) {
                console.error('Error fetching users:', error);
            }
        },
        async createNCC(form) {
            console.log(this.data);
            axios.post(apiNCC, form).then((response) => {
                    this.data.unshift(response.data);
            });
        },
        async updateLoSanPham(id) {
            axios.put(apiNCC`/${idctsp}`).then((response) => {
                const index = this.data.findIndex(product => product.id === idctsp);
                if (index !== -1) {
                    let newProductData = this.data[index];
                    newProductData = response.dataByStatus1;
                    this.data[index] = newProductData;
                }
            });
        }
        ,
        async deleteNCC(id) {
            axios.put(apiNCC`/${idctsp}`).then((response) => {
                const index = this.data.findIndex(product => product.id === idctsp);
                if (index !== -1) {
                    let newProductData = this.data[index];
                    newProductData = response.dataByStatus1;
                    this.data[index] = newProductData;
                }
            });
        }
    }
});
