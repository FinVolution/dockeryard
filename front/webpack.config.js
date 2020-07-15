const path = require('path');
const resolve = require('path').resolve;
const webpack = require('webpack');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const url = require('url');
const publicPath = '';
const request = require("request");

module.exports = (options = {}) => ({
    entry: {
        vendor: './src/vendor',
        index: './src/main.js'
    },
    output: {
        path: resolve(__dirname, '../admin/src/main/resources/static'),
        filename: options.dev ? '[name].js' : '[name].js?[chunkhash]',
        chunkFilename: '[id].js?[chunkhash]',
        publicPath: options.dev ? '/assets/' : publicPath
    },
    module: {
        loaders: [
            {
                test: /\.css$/,
                loader: 'style-loader!css-loader'
            }
        ],
        rules: [
            {
                test: /\.vue$/,
                use: ['vue-loader']
            },
            {
                test: /\.js$/,
                use: ['babel-loader'],
                exclude: /node_modules/
            },
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader', 'postcss-loader']
            },
            {
                test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
                use: [{
                    loader: 'url-loader',
                    options: {
                        limit: 10000
                    }
                }]
            }
        ]
    },
    plugins: [
        new webpack.optimize.CommonsChunkPlugin({
            names: ['vendor', 'manifest']
        }),
        new HtmlWebpackPlugin({
            title: 'My App',
            template: 'src/index.html'
        })
    ],
    resolve: {
        alias: {
            '~': resolve(__dirname, 'src'),
            'vue': 'vue/dist/vue.js',
        }
    },
    devServer: {
        port: 8010,
        contentBase: [path.join(__dirname, "public"), path.join(__dirname, "/assets")],
        proxy: {
            '/api/': {
                target: 'http://localhost:8080/api',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            }
        },
        historyApiFallback: {
            index: url.parse(options.dev ? '/assets/' : publicPath).pathname
        },
        setup(app){
            app.get('/api/token', function (req, res) {

                if ((req.query == null) || (req.query.code == null)) {
                    // exit with 400 bad request error
                    console.log("The user inputs an empty code for query.");
                    res.status(400).end();
                } else {

                    let authCode = req.query.code;

                    let options = {
                        method: 'POST',
                        url: 'http://localhost/oauth2/token',
                        headers: {
                            'authorization': 'Basic Z2VtaW5pLWRldjpzQ2Q5d0k=',
                            'content-type': 'application/x-www-form-urlencoded',
                            'cache-control': 'no-cache'
                        },
                        json: true,
                        form: {
                            'grant_type': 'AUTHORIZATION_CODE',
                            'code': authCode,
                            'redirect_uri': 'http://localhost:8010/#/login',
                        }
                    };

                    request.post(options, function (e, r, response) {
                        //console.log(response);

                        if (response != null && response.access_token != null && response.refresh_token != null) {
                            // console.log(response);
                            res.send(response);
                        } else {
                            // exit with 401 Unauthorized
                            console.log("no access token is returned from sso, please ensure user has input correct auth code.");
                            res.status(401).end();
                        }

                    });

                }


            });

            app.get('/api/refresh', function (req, res) {

                if ((req.query == null) || (req.query.refresh_token == null)) {
                    // exit with 400 bad request error
                    console.log("The user inputs an empty refresh token for query.");
                    res.status(400).end();
                } else {

                    let refresh_token = req.query.refresh_token;

                    let options = {
                        method: 'POST',
                        url: 'http://localhost/oauth2/token',
                        headers: {
                            'authorization': 'Basic Z2VtaW5pLWRldjpzQ2Q5d0k=',
                            'content-type': 'application/x-www-form-urlencoded',
                            'cache-control': 'no-cache'
                        },
                        json: true,
                        form: {
                            'grant_type': 'REFRESH_TOKEN',
                            'refresh_token': refresh_token,
                            'redirect_uri': 'http://localhost:8010/#/login',
                        }
                    };

                    request.post(options, function (e, r, response) {
                        //console.log(response);

                        if (response != null && response.access_token != null) {
                            // console.log(response);
                            res.send(response);
                        } else {
                            // exit with 401 Unauthorized
                            console.log("no access token is returned from sso, please ensure user has input correct auth code.");
                            res.status(401).end();
                        }

                    });

                }


            });
        }
    },
    devtool: options.dev ? '#eval-source-map' : '#source-map'
})
